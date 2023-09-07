package com.lucky.ssyx.common.exception;

import com.lucky.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lucky
 * @date 2023/8/26 14:53
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        return Result.fail(null);
    }

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(SsyxException.class)
    public Result error(SsyxException e){
        return Result.build(null,e.getCode(),e.getMessage());
    }
}
