package com.wei.exception;


import com.wei.response.R;
import com.wei.response.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        return R.fail(ResponseCode.ERROR.getCode(),e.getMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BussinessException.class)
    public R handleBusinessException(BussinessException e){
        return R.fail(e.getCode(),e.getMessage());
    }
}
