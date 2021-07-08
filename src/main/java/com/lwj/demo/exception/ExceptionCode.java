package com.lwj.demo.exception;

public enum ExceptionCode {
    /**
     * rpc异常code
     */
    OPERATE(-1, "操作失败"),
    INSERT(-2, "插入失败"),
    EDIT(-3, "编辑失败"),
    DELETE(-4, "删除失败"),
    TOKEN(-5, "凭证错误"),
    ARGUMENT(-6, "参数错误"),
    REGISTER(-8, "注册失败"),
    FREEZE(-9, "冻结失败"),
    API_VERSION(-10, "api版本校验失败"),
    SMS(-11, "短信发送失败"),
    OUT_PACKET(-12, "出袋异常"),
    UNAUTHORIZED(-403, "未授权"),
    VALID_USER(-1001, "加载第三方授权数据失败"),
    WX_CONFIG_ERROR(-1002, "加载微信配置文件错误"),
    SHORT_URL(-2001, "百度短链接获取失败"),
    LOCK(-2002, "redisson异常"),
    IP_LIMIT(-3001, "ip访问受限"),
    NO_TESTER_PERMISSION(-4001, "无体验者权限"),
    LOCK_TIMEOUT(-5001, "系统繁忙"),
    UPLOAD_FILE(-6001, "上传文件失败"),
    CONVERT_DATE(-7001, "日期转化失败"),
    BUY_FAIL(-8001, "购买失败"),
    TOKEN_EXPIRE(-9001, "TOKEN失效或者已过期"),
    TOKEN_ERROR(-9002, "TOKEN错误");


    private Integer code;
    private String msg;

    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据错误码获取默认错误信息
     *
     * @param code
     * @return
     */
    public static String getMsgByCode(Integer code) {
        for (ExceptionCode exceptionCode : ExceptionCode.values()) {
            if (code.equals(exceptionCode.getCode())) {
                return exceptionCode.getMsg();
            }
        }
        return "未知的错误码";
    }
}
