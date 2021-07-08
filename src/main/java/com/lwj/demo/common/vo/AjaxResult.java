package com.lwj.demo.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * ajax 返回 结构
 */
@Getter
@Setter
public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = 1894648231137314539L;
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回的对象
     */
    private T data;

    /**
     * 失败码
     */
    private static final int FAIL_CODE = -1;

    /**
     * 正确码
     */
    private static final int SUCCESS_CODE = 0;

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> success() {
        return initResult(SUCCESS_CODE, "操作成功", null);
    }

    /**
     * 成功
     *
     * @param message 提示语
     * @param object  返回的数据
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> success(String message, T object) {
        return initResult(SUCCESS_CODE, message, object);
    }

    /**
     * 成功
     *
     * @param object 返回的数据
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> success(T object) {
        return initResult(SUCCESS_CODE, "操作成功", object);
    }

    /**
     * 成功
     *
     * @param message 提示语
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> success(String message) {
        return initResult(SUCCESS_CODE, message, null);
    }


    /**
     * 失败
     *
     * @param message 提示语
     * @param object  返回的数据
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> fail(String message, T object) {
        return initResult(FAIL_CODE, message, object);
    }


    /**
     * 失败
     *
     * @param message 提示语
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> fail(String message) {
        AjaxResult<T> ajaxResult = new AjaxResult();
        ajaxResult.setCode(FAIL_CODE);
        ajaxResult.setMsg(message);
        return initResult(FAIL_CODE, message, null);
    }


    /**
     * 失败
     *
     * @param code
     * @param object
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> fail(int code, T object) {
        AjaxResult<T> ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(null);
        ajaxResult.setData(object);
        return initResult(code, "操作失败", object);
    }

    /**
     * 失败处理
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> failByMsg(int code, String msg) {
        AjaxResult<T> ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(null);
        return initResult(code, msg, null);
    }


    /**
     * 初始化Result对象
     *
     * @param code
     * @param message
     * @param object
     * @param <T>
     * @return
     */
    public static <T> AjaxResult<T> initResult(int code, String message, T object) {
        AjaxResult<T> ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(message);
        ajaxResult.setData(object);
        return ajaxResult;
    }

    public static <T> AjaxResult<T> byBoolean(boolean b) {
        return initResult(b ? SUCCESS_CODE : FAIL_CODE, b ? "操作成功" : "操作失败", null);
    }
}
