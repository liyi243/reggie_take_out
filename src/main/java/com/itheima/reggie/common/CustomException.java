package com.itheima.reggie.common;

/**
 * @ClassName 自定义业务异常
 * @Description  RuntimeException属于非受控异常，系统可以处理也可不处理;受控异常则是必须处理的异常
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
public class CustomException extends RuntimeException{
    //有参构造
    public CustomException(String message) {
        super(message);
    }
}
