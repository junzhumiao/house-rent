package com.jzm.backme.model;

import com.jzm.backme.constant.Constant;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: jzm
 * @date: 2024-03-08 18:04
 **/

@Setter
@Getter
public class PageResult extends AjaxResult
{
    private long total;

    protected PageResult(Object data, int code, String mes,long total)
    {
        super(data, code, mes);
        this.total = total;
    }

    private static PageResult common(Object data, int code, String mes,long total){
        return new PageResult(data,code,mes,total);
    }
    public static PageResult success(Object data,long total){
        return common(data, Constant.OK_CODE,Constant.OK_MES,total);
    }
}
