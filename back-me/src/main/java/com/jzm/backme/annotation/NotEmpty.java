package com.jzm.backme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *空字段校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})

public @interface NotEmpty
{
    // 必填: 就是不为null、""
  boolean required() default true;
  String extra() default "";
}
