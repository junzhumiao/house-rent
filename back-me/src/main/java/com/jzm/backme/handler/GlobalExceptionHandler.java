package com.jzm.backme.handler;

import com.jzm.backme.exception.base.BaseException;
import com.jzm.backme.model.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: jzm
 * @date: 2024-04-18 21:27
 **/

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler(BaseException.class)
    public AjaxResult handlerBaseException(BaseException ex)
    {
        ex.printStackTrace();
        return AjaxResult.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult handlerException(Exception ex)
    {
        ex.printStackTrace();
        return AjaxResult.error(400, ex.getMessage());
    }


}
