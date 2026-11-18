package com.wei.exception;

import com.wei.response.ResponseCode;
import lombok.Data;

@Data
public class BussinessException extends RuntimeException{
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态信息
     */
    private  String message;
    public BussinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public BussinessException(String message) {
        this.message = message;
    }

    public BussinessException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

}
