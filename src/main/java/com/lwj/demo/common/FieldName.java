package com.lwj.demo.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lwj
 * @interfaceName: FieldName
 * @Description: 自定义字段名是否被转成map中的值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FieldName {

    /**
     * 字段名
     */
    String value() default "";

    /**
     * 是否忽略
     */
    boolean ignore() default false;

    /**
     * 时间格式化
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";
}
