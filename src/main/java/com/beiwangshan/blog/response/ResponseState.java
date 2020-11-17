package com.beiwangshan.blog.response;

/**
 * @className: com.beiwangshan.blog.response-> ResponseState
 * @description: 统一返回结果的状态
 * @author: 曾豪
 * @createDate: 2020-11-17 8:54
 * @version: 1.0
 * @todo:
 */
public class ResponseState {
    private int code;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
