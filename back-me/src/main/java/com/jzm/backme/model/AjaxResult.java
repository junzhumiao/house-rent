package com.jzm.backme.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jzm.backme.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 公共响应类
 *
 * @author: jzm
 * @date: 2024-01-08 20:29
 **/

@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 排除值为空字段的序列化
public class AjaxResult implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Object data;
   private   int code;
   private String mes;


    protected AjaxResult(Object data, int code, String mes)
    {
        this.data = data;
        this.code = code;
        this.mes = mes;
    }

    /**
     * 返回默认成功响应
     *
     * @return 成功响应
     */
    public static AjaxResult success()
    {
        return AjaxResult.success(Constant.OK_MES);
    }


    /**
     * 返回成功响应
     *
     * @param mes 成功信息
     * @return 成功响应
     */
    public static AjaxResult success(String mes)
    {
        return AjaxResult.common(null,200, mes);
    }

    /**
     * 返回成功响应
     *
     * @param data 成功内容
     * @return 成功响应
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.common(data,Constant.OK_CODE, Constant.OK_MES);
    }


    /**
     * 返回警告响应
     *
     * @param data 警告内容
     * @param mes  警告信息
     * @return 警告响应
     */
    public static AjaxResult warn(Object data, String mes)
    {
        return AjaxResult.common(data,Constant.WARN_CODE, mes);
    }


    /**
     * 返回警告响应
     *
     * @param mes 警告信息
     * @return 警告响应
     */
    public static AjaxResult warn(String mes)
    {
        return AjaxResult.warn(null, mes);
    }

    /**
     * 返回失败响应
     *
     * @return 失败响应
     */
    public static AjaxResult error()
    {
        return AjaxResult.error(Constant.FAIL_MES);
    }

    /**
     * 返回失败响应
     *
     * @param mes 失败信息
     * @return 失败响应
     */
    public static AjaxResult error(String mes)
    {
        return AjaxResult.common(null,Constant.ERROR_CODE, mes);
    }

    /**
     * 返回失败响应
     *
     * @param code 失败编码
     * @param mes  失败信息
     * @return 失败响应
     */
    public static AjaxResult error(int code, String mes)
    {
        return AjaxResult.common(null, code, mes);
    }


    /**
     * 返回响应
     *
     * @param data 响应内容
     * @param code 响应编码
     * @param mes  响应消息
     * @return 响应
     */
    private static AjaxResult common(Object data, int code, String mes)
    {
        return new AjaxResult(data, code, mes);
    }


}
