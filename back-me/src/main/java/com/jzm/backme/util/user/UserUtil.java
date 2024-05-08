package com.jzm.backme.util.user;


import cn.hutool.crypto.digest.MD5;
import com.jzm.backme.constant.UserConstant;
import com.jzm.backme.util.StringUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户工具类(对用户相关参数的校验)
 *
 * @author: jzm
 * @date: 2024-02-28 10:49
 **/

public class UserUtil
{

    public static String cryptPass(String password){
        return MD5.create().digestHex(password);
    }

    public  static boolean verifyPassword(String password) {
        if (StringUtil.isEmpty(password) ||   password.length() < UserConstant.PASSWORD_MIN_LENGTH || password.length() > UserConstant.PASSWORD_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    public static boolean verifyUsername(String username) {
        if (StringUtil.isEmpty(username) || username.length() <UserConstant.USERNAME_MIN_LENGTH || username.length() > UserConstant.USERNAME_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    public  static  boolean verifyPhone(String phone) {
      return phone.matches(UserConstant.MOBILE_PHONE_NUMBER_PATTERN);
    }

    public static boolean isDelete(String flag){
        return UserConstant.EXCEPTION.equals(flag);
    }

    public static boolean isDisable(String flag){
       return UserConstant.EXCEPTION.equals(flag);
    }


    public static boolean verifyAccount(String tenant)
    {
        if(tenant.startsWith("0x") && tenant.length() == 42){
            return true;
        }
        return false;
    }
}
