package com.jzm.backme.config.fisco.contract;


import com.jzm.backme.exception.base.BaseException;

/**
 * @author: jzm
 * @date: 2024-04-16 19:30
 **/

public class ContractException extends BaseException
{
    public ContractException(String message, int code)
    {
        super(message, code);
    }

    public ContractException(String message)
    {
        super(message, 400);
    }
}
