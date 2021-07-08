package com.lwj.demo.system.config;

import com.lwj.demo.system.entity.DataSourceType;

import java.lang.annotation.*;

/**
 * @author luowj
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MyDataSource {

    /**
     * 数据源key
     */
    DataSourceType type() default DataSourceType.SYSTEM;

    int value() default 0;
}
