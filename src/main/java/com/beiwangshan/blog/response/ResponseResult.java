package com.beiwangshan.blog.response;

/**
 * @className: com.beiwangshan.blog.response-> ResponseResult
 * @description: 统一封装的返回结果
 * @author: 曾豪
 * @createDate: 2020-11-17 8:47
 * @version: 1.0
 * @todo:
 */
public class ResponseResult {
    private boolean success;
    private int code;
    private String message;
    private Object data;

    public ResponseResult(ResponseState responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    /**
     * 成功状态的封装
     * @return
     */
    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResponseState.SUCCESS);
    }

    /**
     * 用户未登录
     * @return
     */
    public static ResponseResult ACCOUNT_NOT_LOGIN(){
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    /**
     * 用户无权限操作
     * @return
     */
    public static ResponseResult PERMISSION_FORBID(){
        return new ResponseResult(ResponseState.PERMISSION_DENIAL);
    }



    /**
     * 成功状态的封装，加上重载，可以传入参数
     * @param message
     * @return
     */
    public static ResponseResult SUCCESS(String message){
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult GET_STATE(ResponseState state){
        return new ResponseResult(state);
    }

    /**
     *    失败状态的封装
     * @return
     */
    public static ResponseResult FAILED(){
        return new ResponseResult(ResponseState.FAILED);
    }


    /**
     * 失败状态的封装，加上重载，可以传入参数
     * @param message
     * @return
     */
    public static ResponseResult FAILED(String message){
        ResponseResult responseResult =new ResponseResult(ResponseState.FAILED);;
        responseResult.setMessage(message);
        return responseResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public Object getData() {
        return data;
    }

    /**
     * 修改返回值
     * @param data
     * @return
     */
    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
