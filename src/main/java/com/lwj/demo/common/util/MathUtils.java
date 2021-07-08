package com.lwj.demo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数字工具
 */
public class MathUtils {

    /**
     * 是否为整数
     *
     * @param value
     * @return
     */
    public static boolean isInt(Object value) {
        return getDouble(value) == getInt(value);
    }


    /**
     * 获取int
     *
     * @param value
     * @return
     */
    public static int getInt(Object value) {
        if (value != null) {
            if (Number.class.isAssignableFrom(value.getClass())) {
                return ((Number) value).intValue();
            } else {
                return Integer.valueOf(String.valueOf(value));
            }
        }

        return 0;
    }

    /**
     * 获取long
     *
     * @param value
     * @return
     */
    public static long getLong(Object value) {
        if (value != null) {
            if (Number.class.isAssignableFrom(value.getClass())) {
                return ((Number) value).longValue();
            } else {
                return Long.valueOf(String.valueOf(value));
            }
        }

        return 0L;
    }


    /**
     * 获取double
     *
     * @param value
     * @return
     */
    public static double getDouble(Object value) {
        if (value != null) {
            if (Number.class.isAssignableFrom(value.getClass())) {
                return ((Number) value).doubleValue();
            } else {
                return Double.valueOf(String.valueOf(value));
            }
        }

        return 0;
    }

    /**
     * 获取BigDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(Object value) {
        if (value != null) {
            if (Number.class.isAssignableFrom(value.getClass())) {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                return new BigDecimal(String.valueOf(value));
            }
        }

        return BigDecimal.ZERO;
    }

    /**
     * 保留几位小数
     *
     * @param d
     * @param i
     * @return
     */
    public static double remainingNum(double d, int i) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(i, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @return
     */
    public static int getMin(Object... objs) {
        int minIndex = 0;
        for (int i = 0; i < objs.length; i++) {
            if (getInt(objs[i]) < getInt(objs[minIndex])) {
                minIndex = i;
            }

        }
        return getInt(objs[minIndex]);
    }


    /**
     * 获取long
     *
     * @param value
     * @return
     */
    public static BigDecimal fenToYun(Object value, RoundingMode roundingMode) {
        return getBigDecimal(value).divide(new BigDecimal(100), roundingMode);
    }

    /**
     * 获取long
     *
     * @param value
     * @return
     */
    public static BigDecimal yunToFen(Object value) {
        return getBigDecimal(value).multiply(new BigDecimal(100));
    }

}
