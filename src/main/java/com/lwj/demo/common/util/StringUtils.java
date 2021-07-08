package com.lwj.demo.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 字符串工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 字符串转Unicode编码
     *
     * @param str
     * @return
     */
    public static String string2Unicode(String str) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            unicode.append("\\u").append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * Unicode编码转字符串
     *
     * @param unicode
     * @return
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取随机数字
     *
     * @param length 长度
     * @return
     */
    public static String getRandomNum(int length) {
        // 35是因为数组是从0开始的，26个字母+10个数字
        int maxNum = 36;
        // 生成的随机数
        int i;
        // 生成的密码的长度
        int count = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder randomStr = new StringBuilder();
        Random r = new Random();
        while (count < length) {
            // 生成随机数，取绝对值，防止生成负数，
            // 生成的数最大为36-1
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                randomStr.append(str[i]);
                count++;
            }
        }
        return randomStr.toString();
    }

    /**
     * 生成随便字母数字
     *
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    /**
     * 多个字符串与比较值进行比较，忽略大小写，如果存在一个值相等的，返回true
     *
     * @param compare 比较值
     * @param strs
     * @return
     */
    public static boolean equalOneIgnoreCase(String compare, String... strs) {
        for (String str : strs) {
            if (compare.equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 多个字符串与比较值进行比较，如果存在一个值相等的，返回true
     *
     * @param compare 比较值
     * @param strs
     * @return
     */
    public static boolean equalOne(String compare, String... strs) {
        for (String str : strs) {
            if (compare.equals(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 多个字符串与比较值进行比较，如果存在一个值相等的，返回true
     *
     * @param compare 比较值
     * @param strs
     * @return
     */
    public static boolean equalOne(char compare, String... strs) {
        String c = String.valueOf(compare);

        for (String str : strs) {
            if (c.equals(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 去掉头尾空格，如果为null，则返回空字符串
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return trimToEmpty(str);
    }

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    public static String upCaseFirst(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    /**
     * 指定删除结尾是什么的字符串 比如 123,==>>123 需要removeLast("123,",",");
     *
     * @return
     */
    public static String removeLast(String str, String key) {

        if (lastIndexOf(str, key) == str.length() - 1) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 移除逗号结尾
     *
     * @param str
     * @return
     */
    public static String removeLastWithComma(String str) {
        return removeLast(str, ",");
    }

    /**
     * 按要求自动补全数值
     *
     * @param number
     * @param length 长度，不够前面加0
     * @return
     */
    public static String fullNumber(String number, int length) {
        StringBuilder sbf = new StringBuilder();
        int l = length - number.length();
        for (int i = 0; i < l; i++) {
            sbf.append("0");
        }
        return sbf.append(number).toString();
    }

    /**
     * 去除enter
     *
     * @param str
     * @return
     */
    public static String removeEnter(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = str.replaceAll("[\r\n]", "");
        }
        return str;
    }

    /**
     * 获取 中文字符的长度，ed=>“测试” return 2,
     *
     * @param text
     * @return
     */
    public final static float getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return (float) length / 2;
    }

    /**
     * 字符串替换，第几个到
     *
     * @param sourceStr  原始字符串
     * @param targetStr  替换成字符串
     * @param startIndex
     * @param endIndex
     * @return
     */
    public  static String replace(String sourceStr, String targetStr, int startIndex, int endIndex) {
        return sourceStr.substring(0, startIndex) + targetStr + sourceStr.substring(endIndex, sourceStr.length());
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */
    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        //定位
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }
}
