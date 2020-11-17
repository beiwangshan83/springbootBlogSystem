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
     * 成功状态的封装，加上重载，可以传入参数
     * @param message
     * @return
     */
    public static ResponseResult SUCCESS(String message){
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     *    失败状态的封装
     * @return
     */
    public static ResponseResult FAILD(){
        return new ResponseResult(ResponseState.FAILD);
    }


    /**
     * 失败状态的封装，加上重载，可以传入参数
     * @param message
     * @return
     */
    public static ResponseResult FAILD(String message){
        ResponseResult responseResult =new ResponseResult(ResponseState.FAILD);;
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
