package com.lwj.demo.common.enums;

/**
 * 用户状态
 */
public enum UserStatus {
    /*
     * 正常
     */
    NORMAL(1),
    /*
     * 软删除
     */
    DELETED(-1),
    /*
     * 锁定
     */
    LOCK(2);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * 判断数值是否属于枚举类的值
     *
     * @param value 值
     * @return 是否是枚举中的值
     */
    public static boolean isInclude(int value) {
        boolean include = false;
        for (UserStatus e : UserStatus.values()) {
            if (e.getValue() == value) {
                include = true;
                break;
            }
        }
        return include;
    }
}
