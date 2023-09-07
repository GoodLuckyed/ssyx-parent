package com.lucky.ssyx.common.result;

/**
 * @author lucky
 * @date 2023/8/26 14:23
 */

import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
public class Result<T> {
    //状态码
    private Integer code;
    //信息
    private String message;
    //数据
    private T data;

    //私有化构造器
    private Result() {
    }

    //设置数据,返回对象的方法
    public static<T> Result<T> build(T data,Integer code,String message){
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    //设置数据,返回对象的方法
    public static<T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //成功的方法
    public static<T> Result<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    //失败的方法
    public static<T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }
}
