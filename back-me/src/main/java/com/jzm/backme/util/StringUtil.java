package com.jzm.backme.util;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * String 工具类
 *
 * @author: jzm
 * @date: 2024-01-08 19:44
 **/

public class StringUtil extends StrUtil
{

    public static boolean equals(Object a,Object b){
        return Objects.equals(a, b) || StringUtil.equals(a.toString(),b.toString());
    }

    /**
     * 是空字符串?
     *
     * @param str
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(CharSequence str)
    {
        if (StrUtil.isEmpty(str))
        {
            return true;
        }
        if (str instanceof String)
        {
            if (((String) str).trim().equals(""))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 是空?
     *
     * @param obj 对象
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object obj)
    {
        return obj == null;
    }

    /**
     * 不是空?
     *
     * @param str
     * @return true：非空 false：为空
     */
    public static boolean isNotEmpty(CharSequence str)
    {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }

    /**
     * 判断字符串列表是否包含被包含字符串
     *
     * @param str  被包含字符串
     * @param strs 字符串列表
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (s.equalsIgnoreCase(str))
                {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isNull(Object obj)
    {
        return obj == null;
    }

    public static boolean isNotNull(Object obj)
    {
        return !isNull(obj);
    }




}
