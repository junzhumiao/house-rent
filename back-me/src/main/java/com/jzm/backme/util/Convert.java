package com.jzm.backme.util;

/**
 * @author: jzm
 * @date: 2024-04-17 08:44
 **/

public class Convert
{
    /**
     * 16进制字符串(0x开头的)-数字
     * @return
     */
    public static Integer hexToInt(String val){
        if(val.startsWith("0x")){
            val = val.substring("0x".length());
        }else{
            return Integer.parseInt(val);
        }
        return Integer.parseInt(val, 16);
    }

    public static Long hexToLong(String val)
    {
        return Long.parseLong(hexToInt(val).toString());
    }


    public static String boolConv(Boolean bool){
        if(!bool){
            return "0";
        }else{
            return "1";
        }
    }


}
