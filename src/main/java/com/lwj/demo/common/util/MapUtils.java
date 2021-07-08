package com.lwj.demo.common.util;

import com.lwj.demo.common.FieldName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwj
 * @ClassName: MapUtil
 * @Description: map的工具类
 */
@Slf4j
public class MapUtils {

    private MapUtils() {

    }

    /**
     * 转换bean为map
     *
     * @param source 要转换的bean
     * @param <T>    bean类型
     * @return 转换结果
     */
    public static <T> Map<String, String> bean2Map(T source) {
        Map<String, String> result = new HashMap<>();

        Class<?> sourceClass = source.getClass();
        //拿到所有的字段,不包括继承的字段
        Field[] sourceFiled = sourceClass.getDeclaredFields();
        try {
            for (Field field : sourceFiled) {
                field.setAccessible(true);
                Object o = field.get(source);
                if (o != null) {
                    //配置了注解的话则使用注解名称
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if (fieldName == null) {
                        result.put(field.getName(), o.toString());
                    } else {
                        if (fieldName.ignore()) {
                            continue;
                        }
                        result.put(fieldName.value(), o.toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), MapUtils.class);
        }
        return result;
    }


    public static <T> Map<String, Object> bean2ObjMap(T source) {
        Map<String, Object> result = new HashMap<>();

        Class<?> sourceClass = source.getClass();
        //拿到所有的字段,不包括继承的字段
        Field[] sourceFiled = sourceClass.getDeclaredFields();
        try {
            for (Field field : sourceFiled) {
                field.setAccessible(true);
                Object o = field.get(source);
                if (o != null) {
                    //配置了注解的话则使用注解名称
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if (fieldName == null) {
                        result.put(field.getName(), o);
                    } else {
                        if (fieldName.ignore()) {
                            continue;
                        }
                        result.put(fieldName.value(), o);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), MapUtils.class);
        }
        return result;
    }

    /**
     * map转bean 自动排除 map中不属于bean的字段
     *
     * @param source   map属性
     * @param instance 要转换成的对象
     * @return 该bean
     */
    public static <T> T map2Bean(Map<String, Object> source, Class<T> instance) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            T object = instance.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                //配置了注解的话则使用注解名称
                FieldName fieldName = field.getAnnotation(FieldName.class);
                String name = field.getName();
                if (fieldName != null) {
                    if (fieldName.ignore()) {
                        continue;
                    }
                    String value = fieldName.value().trim();
                    if (!"".equals(value)) {
                        name = value;
                    }
                }
                if (!source.containsKey(name)) {
                    continue;
                }
                setValue(object, field, source);
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), MapUtils.class);
        }
        return null;
    }

    /**
     * 将多个map组成一个map
     *
     * @param source map
     * @return 组合之后的Map
     */
    public static Map<String, Object> composeAllMap(List<Map<String, Object>> source) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (source != null && !source.isEmpty()) {
            for (Map<String, Object> map : source) {
                hashMap.putAll(map);
            }
            return hashMap;
        }
        return Collections.emptyMap();
    }

    public static boolean isContainerKey(Map<String, Object> map, String key) {
        if (map == null) {
            return false;
        }
        return map.containsKey(key);
    }

    private static <T> void setValue(T object, Field field, Map<String, Object> source) {
        try {
            Method setMet = object.getClass().getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
            //取得数据类型
            String type = field.getType().getSimpleName();
            FieldName annotation = field.getAnnotation(FieldName.class);
            String name = field.getName();
            if (annotation != null) {
                String value = annotation.value().trim();
                if (!"".equals(value)) {
                    name = value;
                }
            }
            Object o = source.get(name);
            if (o == null || "".equals(o.toString().trim())) {
                return;
            }
            if ("int".equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
                setMet.invoke(object, Integer.parseInt(source.get(name).toString()));
            } else if ("double".equalsIgnoreCase(type)) {
                setMet.invoke(object, Double.parseDouble(source.get(name).toString()));
            } else if ("long".equalsIgnoreCase(type)) {
                setMet.invoke(object, Long.parseLong(source.get(name).toString()));
            } else if ("BigDecimal".equalsIgnoreCase(type)) {
                setMet.invoke(object, BigDecimal.valueOf(Double.parseDouble(source.get(name).toString())));
            } else if ("date".equalsIgnoreCase(type)) {
                String date = source.get(name).toString();
                SimpleDateFormat dateFormat;
                if (annotation != null) {
                    dateFormat = new SimpleDateFormat(annotation.dateFormat());
                } else {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                }
                setMet.invoke(object, dateFormat.parse(date));
            } else {
                setMet.invoke(object, source.get(name));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
