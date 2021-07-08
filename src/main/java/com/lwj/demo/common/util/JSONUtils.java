package com.lwj.demo.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class JSONUtils {

    private JSONUtils() {

    }

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        // 使用和json-lib兼容的日期输出格式
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty,
            // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 转换成飞空的字段
     *
     * @param object 对象
     * @return java.lang.String
     */
    public static String convertObjectToJson(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 空字段直接输出null
     *
     * @param object
     * @return java.lang.String
     */
    public static String toJsonNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }


    /**
     * 获取JSON格式字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        }
    }


    /**
     * 获取JSON格式字符串的byte
     *
     * @param obj
     * @return
     */
    public static byte[] toJsonByte(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue).getBytes();
        }
    }

    /**
     * 获取JSON格式字符串的byte, null的字段 不显示
     *
     * @param obj
     * @return
     */
    public static byte[] toJsonNotNullByte(Object obj) {
        if (obj == null) {
            return new byte[0];
        } else {
            return JSON.toJSONString(obj).getBytes();
        }
    }

    /**
     * JSON格式字符串转换成目标对象
     *
     * @param <T>
     * @param jsonStr JSON格式字符串
     * @param cls     目标对象类型
     * @return
     */
    public static <T> T toObject(String jsonStr, Class<T> cls) {
        return JSONObject.parseObject(jsonStr, cls);
    }

    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param text
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toBean(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type);
    }

    /**
     * JSON格式字符串转换成目标集合对象
     *
     * @param <T>
     * @param jsonStr JSON格式字符串
     * @param cls     目标对象类型
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> cls) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        return JSONArray.parseArray(jsonStr, cls);
    }


}
