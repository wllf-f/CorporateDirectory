package com.wei.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R<T> {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态信息
     */
    private String message;
    /**
     * 数据
     */
    private T data;
    
    private R(Integer code){
        this.code = code;
    }

    private R(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    
    private R(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> R<T> success(){
        R<T> r = new R<>(ResponseCode.SUCCESS.getCode());
        r.setMessage(ResponseCode.SUCCESS.getMessage());
        return r;
    }
    
    public static <T> R<T> success(String message){
        R<T> r = new R<>(ResponseCode.SUCCESS.getCode());
        r.setMessage(message);
        return r;
    }
    
    public static <T> R<T> data(T data){
        R<T> r = new R<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        r.setData(data);
        return r;
    }
    
    public static <T> R<T> fail(){
        R<T> r = new R<>(ResponseCode.ERROR.getCode());
        r.setMessage(ResponseCode.ERROR.getMessage());
        return r;
    }
    
    public static <T> R<T> fail(String message){
        R<T> r = new R<>(ResponseCode.ERROR.getCode());
        r.setMessage(message);
        return r;
    }
    
    public static <T> R<T> fail(ResponseCode responseCode){
        R<T> r = new R<>(responseCode.getCode());
        r.setMessage(responseCode.getMessage());
        return r;
    }
    
    public static <T> R<T> fail(Integer code,String message){
        R<T> r = new R<>(code);
        r.setMessage(message);
        return r;
    }

}