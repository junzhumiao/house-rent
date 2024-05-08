package com.jzm.backme.exception;

import com.jzm.backme.exception.base.BaseException;

/**
 * @author: jzm
 * @date: 2024-04-14 20:47
 **/

public class CustomException extends BaseException
{

    public CustomException(String message)
    {
        super(message,400);
    }
}
