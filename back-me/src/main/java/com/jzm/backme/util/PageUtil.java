package com.jzm.backme.util;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 分页工具类
 */
public class PageUtil
{
    private static <T> Page<T> _startPage(int pageNum, int pageSize, ISelect select, String orderBy)
    {
        clearPage();
        Page<T> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(select);
        if (orderBy == null)
        {
            return page;
        }
        page.setOrderBy(orderBy);
        return page;
    }

    /**
     * 分页
     *
     * page 起始页
     * pageSize 页大小
     *
     * @param select : () -> userMapper.list()
     */
    public static <T> PageInfo<T> startPage(int pageNum, int pageSize, ISelect select)
    {
        return new PageInfo<>(_startPage(pageNum, pageSize, select, null));
    }

    /**
     * 降序 + 分页
     */
    public static <T> PageInfo<T> orderByDesc(int pageNum, int pageSize, String filed, ISelect select)
    {
        return new PageInfo<>(_startPage(pageNum, pageSize, select, filed + " desc"));
    }


    /**
     * 升序 + 分页
     */
    public static <T> PageInfo<T> orderByAsc(int pageNum, int pageSize, String filed, ISelect select)
    {
        return new PageInfo<>(_startPage(pageNum, pageSize, select, filed + " asc"));
    }

    /**
     * 清理分页的线程变量(清除分页数据)
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }


}
