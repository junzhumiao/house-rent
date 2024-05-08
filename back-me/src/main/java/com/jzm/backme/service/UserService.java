package com.jzm.backme.service;

import com.jzm.backme.domain.Role;
import com.jzm.backme.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzm.backme.model.to.user.PassTo;
import com.jzm.backme.model.to.user.UserPassTo;
import com.jzm.backme.model.to.user.UserQueryTo;

import java.util.List;

/**
 * <p>
 * 用户表 -- 存储用户信息 服务类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
public interface UserService extends IService<User> {

    User getOne(String phone, String password);

    List<User> getAll(UserQueryTo queryTo);

    boolean checkPhoneUnique(String phone);
    boolean checkPhoneUpUnique(String phone);

    boolean checkPassUnique(String password);
    boolean isPasswordExists(String oldPassword);

    boolean checkMd5PassUnique(String password);

    boolean checkUserUnique(String user);

    Role getRole(Long userId);

    boolean updateStatus(User user);
    boolean updatePass(UserPassTo upt);

    boolean updatePass(PassTo passTo);

}
