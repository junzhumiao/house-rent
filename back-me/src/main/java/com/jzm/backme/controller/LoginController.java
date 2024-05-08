package com.jzm.backme.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
import com.jzm.backme.constant.CacheConstant;
import com.jzm.backme.constant.HttpStatus;
import com.jzm.backme.controller.base.BaseController;
import com.jzm.backme.domain.Role;
import com.jzm.backme.domain.User;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.to.LoginTo;
import com.jzm.backme.model.to.user.PassTo;
import com.jzm.backme.model.vo.UserVo;
import com.jzm.backme.service.LoginService;
import com.jzm.backme.service.MainContractService;
import com.jzm.backme.service.UserService;
import com.jzm.backme.util.NotEmptyUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.TokenUtil;
import com.jzm.backme.util.redis.RedisCache;
import com.jzm.backme.util.user.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * @author: jzm
 * @date: 2024-04-14 15:50
 **/

@RestController
public class LoginController extends BaseController
{
    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MainContractService mainContractService;


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public AjaxResult login(@RequestBody LoginTo loginTo)
    {
        // 校验验证码
        HttpStatus errStatus = loginService.verifyCaptcha();
        if (StringUtil.isNotEmpty(errStatus))
        {
            return errStatus;
        }
        String phone = loginTo.getPhone();
        String password = loginTo.getPassword();
        String errMes = NotEmptyUtil.checkEmptyFiled(loginTo);
        if (StringUtil.isNotEmpty(errMes))
        {
            return AjaxResult.error(errMes);
        }
        if (!UserUtil.verifyPhone(phone))
        {
            return HttpStatus.PHONE_NOT_MATCH;
        }
        if (!UserUtil.verifyPassword(password))
        {
            return HttpStatus.PASSWORD_NOT_MATCH;
        }
        User user = userService.getOne(phone, password);
        if (StringUtil.isEmpty(user))
        {
            return HttpStatus.USER_NOT_EXISTS;
        }
        if (UserUtil.isDisable(user.getStatus()))
        {
            return HttpStatus.USER_BLOCKED;
        }

        // 生成token、缓存redis
        Long userId = user.getUserId();
        Role role = userService.getRole(userId);
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(role, userVo);
        BeanUtil.copyProperties(user, userVo);
        int balance = mainContractService.getBalance(user.getUser());
        userVo.setBalance(balance);
        // 缓存用户信息
        redisCache.setCacheObject(CacheConstant.LOGIN_USER_KEY + userId, userVo);
        String token = TokenUtil.createToken(userId);
        HashMap<String, Object> res = new HashMap<>();
        res.put("token", token);
        return HttpStatus.success(res);
    }


    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public AjaxResult logout()
    {
        Long userId = loginService.getUserId();
        if (StringUtil.isEmpty(userId))
        {
            return AjaxResult.success("账户退出成功");
        }
        redisCache.deleteObject(CacheConstant.LOGIN_USER_KEY + userId);
        return AjaxResult.success("账户退出成功");
    }

    @RequestMapping(path = "/getUserInfo", method = RequestMethod.GET)
    public AjaxResult getUserInfo()
    {
        Long userId = loginService.getUserId();
        User user = redisCache.getCacheObject(CacheConstant.LOGIN_USER_KEY + userId);
        return AjaxResult.success(user);
    }


    @RequestMapping(path = "/updateUser", method = RequestMethod.POST)
    public AjaxResult updateUser(@RequestBody User user)
    {
        String phone = user.getPhone();
        String username = user.getUsername();

        if (!StringUtil.isAllNotEmpty(username, phone))
        {
            return error("用户名、电话为必填项!");
        } else if (!UserUtil.verifyPhone(phone))
        {
            return error("电话格式不正确!");
        } else if (!userService.checkPhoneUpUnique(phone))
        {
            return error("电话已经被其他账户绑定!");
        }
        boolean end = userService.updateById(user);
        return toAjax(end);
    }


    @RequestMapping(path = "/updatePass", method = RequestMethod.POST)
    @ApiOperation("用户修改密码")
    public AjaxResult updatePassword(@RequestBody PassTo passTo)
    {
        String oldPassword = passTo.getOldPassword();
        String newPassword = passTo.getNewPassword();
        String errPre = "用户修改密码错误：";
        if (StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword))
        {
            return error(errPre + "用户密码不能为空");
        }
        if (!UserUtil.verifyPassword(oldPassword))
        {
            return error(errPre + "旧密码格式不对!");
        }
        if (!UserUtil.verifyPassword(newPassword))
        {
            return error(errPre + "新密码格式不对!");
        }
        if (StringUtil.equals(newPassword, oldPassword))
        {
            return success();
        }
        if (!userService.isPasswordExists(oldPassword))
        {
            return error(errPre + "旧密码不存在!");
        }
        if (!userService.checkPassUnique(newPassword))
        {
            return error(errPre + "新密码已经被其他用户注册!");
        }
        boolean end = userService.updatePass(passTo);
        return toAjax(end);
    }


}
