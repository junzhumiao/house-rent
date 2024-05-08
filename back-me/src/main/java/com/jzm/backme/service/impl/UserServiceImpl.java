package com.jzm.backme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.jzm.backme.domain.Role;
import com.jzm.backme.domain.User;
import com.jzm.backme.mapper.UserMapper;
import com.jzm.backme.model.to.user.PassTo;
import com.jzm.backme.model.to.user.UserPassTo;
import com.jzm.backme.model.to.user.UserQueryTo;
import com.jzm.backme.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 -- 存储用户信息 服务实现类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getOne(String phone, String password)
    {
        password = UserUtil.cryptPass(password);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone).eq(User::getPassword, password);

        return this.getOne(queryWrapper);
    }

    @Override
    public List<User> getAll(UserQueryTo queryTo)
    {
        String phone = queryTo.getPhone();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(StringUtil.isNotEmpty(phone), User::getPhone, phone);
        return this.list(queryWrapper);
    }

    @Override
    public boolean checkPhoneUnique(String phone)
    {
        return checkFieldUnique(User::getPhone,phone);
    }

    @Override
    public boolean checkPhoneUpUnique(String phone)
    {
        User one = getOne(User::getPhone, phone);
        if( StringUtil.isEmpty(one) || one.getPhone().equals(phone)){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPassUnique(String password)
    {
        password = UserUtil.cryptPass(password);
        return checkMd5PassUnique(password);
    }

    @Override
    public boolean isPasswordExists(String oldPassword)
    {
        return !checkPassUnique(oldPassword);
    }

    @Override
    public boolean checkMd5PassUnique(String password)
    {
        return checkFieldUnique(User::getPassword,password);
    }

    @Override
    public boolean checkUserUnique(String user)
    {
       return checkFieldUnique(User::getUser,user);
    }

    private boolean checkFieldUnique(SFunction<User,?> column,Object val){
        User one = getOne(column,val);
        if(StringUtil.isEmpty(one)){
            return true;
        }
        return false;
    }

    private User getOne(SFunction<User,?> column,Object val){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(column, val);
        return this.getOne(queryWrapper);
    }

    @Override
    public Role getRole(Long userId)
    {
        return userMapper.selectRole(userId);
    }

    @Override
    public boolean updateStatus(User user)
    {
        Long userId = user.getUserId();
        LambdaQueryWrapper<User> updateWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserId, userId);
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setStatus(user.getStatus());
        return this.update(newUser,updateWrapper);
    }

    @Override
    public boolean updatePass(UserPassTo upt)
    {
        String password = upt.getPassword();
        String newPassword = upt.getNewPassword();
        LambdaQueryWrapper<User> updateWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getPassword, password);
        User newUser = new User();
        newUser.setPassword(newPassword);
        return this.update(newUser,updateWrapper);
    }

    @Override
    public boolean updatePass(PassTo passTo)
    {
        String oldPassword = passTo.getOldPassword();
        oldPassword = UserUtil.cryptPass(oldPassword);

        String newPassword = passTo.getNewPassword();
        newPassword = UserUtil.cryptPass(newPassword);

        UserPassTo userPassTo = new UserPassTo(oldPassword, newPassword);
        return updatePass(userPassTo);
    }


}
