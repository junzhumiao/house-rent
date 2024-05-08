package com.jzm.backme.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzm.backme.controller.base.BaseController;
import com.jzm.backme.domain.Role;
import com.jzm.backme.model.to.user.*;
import com.jzm.backme.domain.User;
import com.jzm.backme.domain.UserRole;
import com.jzm.backme.exception.CustomException;
import com.jzm.backme.exception.PermissionException;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.vo.UserVo;
import com.jzm.backme.service.*;
import com.jzm.backme.util.NotEmptyUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.user.UserUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户表 -- 存储用户信息 前端控制器
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private LoginService loginService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private MainContractService mainContractService;


    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public AjaxResult getAll(@RequestBody UserQueryTo queryTo){
        loginService.checkAdmin();
        int page = queryTo.getPage();
        int pageSize = queryTo.getPageSize();
        List<UserVo> userVos = startOrderPage(page, pageSize, UserVo.class, () ->
        {
            userService.getAll(queryTo);
        });
        return getAll(userVos);
    }

    // 重新粘贴userVos
    public AjaxResult getAll(List<UserVo> userVos){
        for (UserVo userVo : userVos)
        {
            String user = userVo.getUser();
            // 账户余额
            int balance = mainContractService.getBalance(user);
            // 角色
            Role role = userService.getRole(userVo.getUserId());
            BeanUtil.copyProperties(role,userVo);
            userVo.setBalance(balance);
        }
        return success(userVos,userVos.size());
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public AjaxResult create(@RequestBody UserAddTo user){
        loginService.checkAdmin();
        String errPre = "添加用户失败:";
        String errMes = NotEmptyUtil.checkEmptyFiled(user);
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();
        if(StringUtil.isNotEmpty(errMes)){
            return error(errPre+errMes);
        }
        if(!UserUtil.verifyPassword(password)){
            return error(errPre+"密码格式不对");
        }
        if(!UserUtil.verifyPhone(phone)){
            return error(errPre+"电话格式不对");
        }
        if(StringUtil.isNotEmpty(username) && !UserUtil.verifyUsername(username)){
            return error(errPre +"用户名格式不对");
        }
        if(!UserUtil.verifyAccount(user.getUser())){
            return error(errPre +"账户地址不是合法的区块链账户地址");
        }
        if(!userService.checkPhoneUnique(phone)){
            return error(errPre + "电话已经被其他账户绑定");
        }
        if(!userService.checkPassUnique(password)){
            return error(errPre +"密码已经被其他用户注册");
        }

        if(!userService.checkUserUnique(user.getUser())){
            return error(errPre + "区块链账户地址已经被其他账户绑定");
        }
        if(StringUtil.isEmpty(username)){
            int l = phone.length();
            user.setUsername("用户:"+phone.substring(l-4,l));
        }
        user.setPassword( UserUtil.cryptPass(password));
        boolean end = userService.save(user);
        // 创建角色
        if(!end){
            throw new CustomException(errPre +"数据库异常");
        }
        mainContractService.addPerson(user.getUser(),user.getRoleId());

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(user.getRoleId());
        userRoleService.save(userRole);
        return success();
    }

    @RequestMapping(value = "/payMoney",method = RequestMethod.POST)
    public AjaxResult payMoney(@RequestBody UserPayMoneyTo ut){
        loginService.checkAdmin();
        String errPre = "重置失败：";
        String userAdd = ut.getUser();
        int amount = ut.getAmount();
        if(amount < 1){
            return error(errPre+"充值金额至少大于等于1");
        }
        mainContractService.addBalance(userAdd,amount);
        return AjaxResult.success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public AjaxResult update(@RequestBody User user){ // 不修改区块链账户,username、password、phone
        loginService.checkAdmin();
        // 修改用户失败
        String errPre = "修改用户失败:";
        if(!userService.checkPhoneUpUnique(user.getPhone())){
            return error(errPre + "电话已经被其他用户绑定!");
        }

        if(!UserUtil.verifyUsername(user.getUsername())){
            return error(errPre +"用户名格式不对");
        }
        LambdaQueryWrapper<User> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(User::getUserId,user.getUserId());
        boolean end = userService.update(user, updateWrapper);
        return toAjax(end);
    }

    @RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
    public AjaxResult  updateStatus(@RequestBody User user){
       boolean end =  userService.updateStatus(user);
       return toAjax(end);
    }

    @RequestMapping(value = "/updatePass",method = RequestMethod.POST)
    public AjaxResult  updatePass(@RequestBody UserPassTo upt){
        String errPre = "修改密码失败：";
        String password = upt.getPassword();
        String newPass = upt.getNewPassword();
        if(!StringUtil.isAllNotEmpty(password,newPass)){
            return error(errPre +"密码不能为空");
        }
        if(StringUtil.equals(password,newPass)){
            return success();
        }
        password = UserUtil.cryptPass(upt.getPassword());
        newPass = UserUtil.cryptPass(upt.getNewPassword());
        if(userService.checkMd5PassUnique(password)){
            return error(errPre +"旧密码不存在");
        }
        if(userService.checkMd5PassUnique(newPass)) {
            return error(errPre +"新密码已经被其他账户绑定");
        }
        upt.setNewPassword(newPass);
        boolean end = userService.updatePass(upt);
        return toAjax(end);
    }



    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public AjaxResult delete(@RequestBody UserDeleteTo userDeleteTo){
        loginService.checkAdmin();
        List<Long> userIds = userDeleteTo.getUserIds();
        for (Long userId : userIds)
        {
            if(StringUtil.equals(userId,loginService.getUserId())){ // 操作者权限不足,不能删除自己
                throw new PermissionException("操作者不能删除自己");
            }
        }
        boolean end = userService.removeBatchByIds(userIds);
        return toAjax(end);
    }


    public static void main(String[] args)
    {
        System.out.println(UserUtil.cryptPass("123457"));
    }



}
