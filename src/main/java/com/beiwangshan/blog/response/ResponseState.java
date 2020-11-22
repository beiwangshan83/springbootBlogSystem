package com.beiwangshan.blog.response;

/**
 * @className: com.beiwangshan.blog.response-> ResponseState
 * @description: 统一返回结果的状态
 * @author: 曾豪
 * @createDate: 2020-11-17 8:54
 * @version: 1.0
 * @todo:
 */
public enum  ResponseState {
    /**
     * 因为一般 2开头的 http状态为成功，为了区别，这里定义为20000
     * 4开头的http状态为失败，所以这里定义为40000
     */
    SUCCESS(true,20000,"操作成功"),
    LOGIN_SUCCESS(true,20001,"登录成功"),
    JOIN_IN_SUCCESS(true,20002,"注册成功"),
    FAILD(false,40000,"操作失败"),
    LOGIN_FAILD(false,40001,"登录失败"),
    REGISTER_FAILD(false,40002,"注册失败"),
    ACCOUNT_NOT_LOGIN(false,40003,"账号未登录"),
    PERMISSION_FORBID(false,40004,"无权访问"),
    GET_RESOURCES_FAILD(false,49999,"获取资源失败");

    private int code;
    private String message;
    private boolean success;

    ResponseState(boolean success,int code,String message){
        this.code=code;
        this.success=success;
        this.message=message;
    }

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
