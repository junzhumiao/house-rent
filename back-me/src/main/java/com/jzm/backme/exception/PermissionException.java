package com.jzm.backme.exception;


import com.jzm.backme.exception.base.BaseException;

/**
 * @author: jzm
 * @date: 2024-03-07 20:34
 **/

public class PermissionException extends BaseException
{
    public PermissionException(String message, int code)
    {
        super(message, code);
    }

    public PermissionException(String message)
    {
        super(message);
    }

    public PermissionException(){
        this("操作者权限不足");
    }
}
