package com.jzm.backme.controller.base;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.PageResult;
import com.jzm.backme.util.PageUtil;

import java.util.List;

/**
 * @author: jzm
 * @date: 2024-04-14 17:41
 **/

public class BaseController
{

    private final String defSortField = "createTime";

    /**
     * 设置请求分页数据
     */
    protected PageInfo startPage(int pageNum, int pageSize, ISelect select)
    {
        return PageUtil.startPage(pageNum, pageSize, select);
    }

    /**
     * 设置请求分页数据库 + 默认排序字段createTime + 升序排序
     */
    protected  PageInfo startOrderPage(int pageNum, int pageSize, ISelect select)
    {
        return startOrderByDesc(pageNum,pageSize,defSortField,select);
    }

    protected <T> List<T> startOrderPage(int pageNum, int pageSize,Class<T> cls, ISelect select)
    {
        return startOrderPage(pageNum,pageSize,cls,defSortField,select);
    }

    /**
     * 分页返回原始查询
     */
    protected <T> List<T> startOrderPage(int pageNum, int pageSize,Class<T> cls,String filed, ISelect select)
    {
        PageInfo pageInfo = startOrderByDesc(pageNum, pageSize, filed, select);
        List list = pageInfo.getList();
        List<T> resList = BeanUtil.copyToList(list, cls);
        return resList;
    }


    /**
     * 设置请求降序排序
     */
    protected PageInfo startOrderByDesc(int pageNumb, int pageSize, String filed, ISelect select)
    {
        return PageUtil.orderByDesc(pageNumb, pageSize, filed, select);
    }


    /**
     * 设置请求升序排序
     */
    protected PageInfo startOrderByAsc(int pageNumb, int pageSize, String filed, ISelect select)
    {
        return PageUtil.orderByAsc(pageNumb, pageSize, filed, select);
    }


    /**
     * 清理线程变量
     */
    protected void cleanPage()
    {
        PageUtil.clearPage();
    }


    protected PageResult success(Object data,long total){
        return PageResult.success(data,total);
    }


    /**
     * 返回成功响应结果
     *
     * @return 成功响应
     */
    protected AjaxResult success()
    {
        return AjaxResult.success();

    }

    /**
     * 返回成功响应结果
     *
     * @param mes 成功消息
     * @return 成功响应
     */
    protected AjaxResult success(String mes)
    {
        return AjaxResult.success(mes);
    }

    /**
     * 返回成功响应
     *
     * @param data 成功内容
     * @return 成功响应
     */
    protected AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回警告响应
     *
     * @param mes 警告信息
     * @return 警告响应
     */
    protected AjaxResult warn(String mes)
    {
        return AjaxResult.warn(mes);
    }

    /**
     * 返回警告响应
     *
     * @param data 警告内容
     * @param mes  警告信息
     * @return 警告响应
     */
    protected AjaxResult warn(Object data, String mes)
    {
        return AjaxResult.warn(data, mes);
    }


    /**
     * 返回失败响应结果
     *
     * @return 失败响应
     */
    protected AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回失败响应结果
     *
     * @param mes
     * @return 失败响应
     */
    protected AjaxResult error(String mes)
    {
        return AjaxResult.error(mes);
    }

    /**
     * 最后返回响应结果
     *
     * @param data 响应数据
     * @return 响应结果
     */
    protected AjaxResult toAjax(Object data)
    {
        return success(data);
    }

    protected PageResult toAjax(PageInfo pageInfo){
        return PageResult.success(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 最后返回响应结果
     *
     * @param results 增、删、改...等修改行为的响应结果
     * @return 响应结果
     */
    protected AjaxResult toAjax(boolean results)
    {
        return results ? success() : error();
    }


}
