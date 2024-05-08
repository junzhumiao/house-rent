package com.jzm.backme.exception.base;


import lombok.Data;

/**
 * 基础异常
 * 
 * @author jzm
 */
@Data
public class BaseException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private String mes;
    private int code;

    public BaseException(String message, int code){
        super(message);
        this.mes = message;
        this.code = code;
    }

    public BaseException(String message){
        super(message);
        this.mes = message;
        this.code = 400;
    }

}
