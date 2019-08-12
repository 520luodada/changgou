package com.changgou.exception;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseExceptionHandler {

    // 异常处理注解
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result baseException(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());


    }
}
