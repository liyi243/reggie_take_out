package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @ClassName 全局异常处理
 * @Description @ResponseBody返回json数据
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExpectionHandler {
    /**
    * @Description: SQLIntegrityConstraintViolationException异常
    * @Description: 异常处理方法
    * @return: com.itheima.reggie.common.R<java.lang.String>
    * @Author: liyunzhi
    * @Date: 2022/6/24
    */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //判断异常是否为Duplicate entry，然后拿出重复的字段
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * @Description: 处理自定义异常CustomException异常
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @Author: liyunzhi
     * @Date: 2022/6/24
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
