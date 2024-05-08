package com.jzm.backme.context;

import com.jzm.backme.domain.User;

/**
 * @author: jzm
 * @date: 2024-04-14 15:41
 **/

public class UserContext
{

    private static ThreadLocal<User> local = new ThreadLocal<>();

    public static  User get(){
        return local.get();
    }

    public static  void set(User user){
         local.set(user);
    }
}
