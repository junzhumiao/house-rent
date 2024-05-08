package com.jzm.backme.util;

import com.jzm.backme.annotation.NotEmpty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * <p>自定义字段检验工具类<p/>
 *
 * 配合 @NotEmpty + 反射：实现被该注解注释字段,校验空值。如果该注解没元数据
 * 则: 字段名 + 不能为空
 *
 *
 * @author: jzm
 * @date: 2024-03-08 10:56
 **/

@Slf4j
public class NotEmptyUtil
{


    public static String checkEmptyFiled(Object obj,String ...fileNames)
    {
        return  checkEmptyFiled(obj,obj.getClass(),fileNames);
    }

    /**
     * 检验字段是否空
     *
     * @param obj 检验对象
     * @param fileNames 忽略字段列表
     * @return 第一个空字段
     */
    public static String checkEmptyFiled(Object obj,Class<?> cls,String ...fileNames)
    {
        NotEmpty notEmptyCls = cls.getAnnotation(NotEmpty.class); // 类获取的空
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            try
            {
                // 忽略字段
                String fileName = field.getName();
                if(StringUtil.equalsAny(fileName,fileNames))
                {
                   continue;
                }
                // 获取字段值
                Object value = field.get(obj);
                // 其他类型(不校验非引用类型)
                if(StringUtil.isEmpty(value))
                {
                    String end = checkFileIsAn(field); // 检查字段是否注解
                    if(end != null)
                    {
                        return end;
                    }
                    if(notEmptyCls != null && notEmptyCls.required()){ // 检查是否类标记注解
                        return fileName + "不能为空";
                    }
                }
                // 字符串类型: "" 值
                if(value instanceof  String){
                   if(StringUtil.isEmpty(value.toString()))
                   {
                       String end = checkFileIsAn(field); // 检查字段是否注解
                       if(end  != null)
                       {
                           return end;
                       }
                       if(notEmptyCls != null && notEmptyCls.required()){ // 检查是否类标记注解
                           return fileName + "不能为空";
                       }
                   }
                }
                // 是对象类型校验对象
                //if(value != null && value.getClass().isInstance(value)){
                //    Class<?> fieldType = value.getClass();
                //    String res = checkEmptyFiled(value, fieldType, fileNames);
                //    if(StringUtil.isNotEmpty(res)){
                //        return res;
                //    }
                //}

            } catch (IllegalAccessException e)
            {
                log.error("读取字段异常:"+e.getMessage());
                return null;
            } catch (Exception e)
            {
               log.error(e.getMessage());
            }
        }
        if( cls.getSuperclass() == Object.class) // 到这结束递归
        {
            return null;
        }
        return  checkEmptyFiled(obj,cls.getSuperclass(),fileNames);
    }


    private static String checkFileIsAn(Field field)
    {
        NotEmpty verifyField = field.getAnnotation(NotEmpty.class);
        if (verifyField != null && verifyField.required()){
            if(StringUtil.isEmpty(verifyField.extra()))
            {
                return field.getName() + "不能为空！";
            }
            return verifyField.extra() + "不能为空！" ;
        }
        return null;
    }




}
