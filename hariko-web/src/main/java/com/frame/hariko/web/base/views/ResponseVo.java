package com.frame.hariko.web.base.views;

import org.springframework.http.HttpStatus;

import com.frame.hariko.core.support.MyThreadContext;

public class ResponseVo<T> {
    private String code = HttpStatus.OK.toString();
    private String message = "success";
    private T data;
    private String  traceId ;
    private long timestamp = System.currentTimeMillis();

    /**
     * 此版本数据主要是用来前端兼容,便于前端区分是老格式的json还是新框架的json格式;
     */
    private int version = ResponseVersion.VERSION_4.getVersion();

    public ResponseVo(){
    }

    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>();
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(data);
    }
    public static <T> ResponseVo<T> fail(String code) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.code = code;
        responseVo.message = "fail";
        return responseVo;
    }
    public static <T> ResponseVo<T> fail(String code, String message) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.code = code;
        responseVo.message = message;
        return responseVo;
    }
    public static <T> ResponseVo<T> fail(String code, T data, String message) {
        ResponseVo<T> responseVo = new ResponseVo<>(data, message);
        responseVo.code = code;
        return responseVo;
    }
    public ResponseVo(T data){
        this.data = data;
    }

    public ResponseVo(T data, String message){
        this.data = data;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTraceId() {
        traceId = MyThreadContext.getTraceId();
        return traceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getVersion() {
        return version;
    }
    
    public boolean isSuccess() {
        return null != this.getCode() && HttpStatus.OK.toString().equals(this.getCode());
    }
}
