package com.lwj.demo.common.util;

import java.math.BigInteger;

/**
 * 权限计算帮助类
 */
public class RightsUtils {

    /**
     * @param rights
     * @return
     */
    public static BigInteger sumRights(Object... rights) {
        return sumRightsPermission(rights);
    }

    /**
     * 位移权限
     *
     * @param rights
     * @return
     */
    public static BigInteger sumRights(String[] rights) {
        return sumRightsPermission(rights);
    }

    private static BigInteger sumRightsPermission(Object[] rights) {
        BigInteger num = new BigInteger("0");
        for (int i = 0; i < rights.length; i++) {
            num = num.setBit(MathUtils.getInt(rights[i]));
        }
        return num;
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetRights
     * @return
     */
    public static boolean testRights(BigInteger sum, int targetRights) {
        return sum.testBit(targetRights);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetRights
     * @return
     */
    public static boolean testRights(String sum, int targetRights) {
        if (StringUtils.isEmpty(sum)) {
            return false;
        }
        return testRights(new BigInteger(sum), targetRights);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetRights
     * @return
     */
    public static boolean testRights(String sum, String targetRights) {
        if (StringUtils.isEmpty(sum)) {
            return false;
        }
        return testRights(new BigInteger(sum), targetRights);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetRights
     * @return
     */
    public static boolean testRights(BigInteger sum, String targetRights) {
        return testRights(sum, Integer.parseInt(targetRights));
    }
}
