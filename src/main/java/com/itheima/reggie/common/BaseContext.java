package com.itheima.reggie.common;

/**
 * @ClassName 基于ThreadLocal封装的工具类，用户保存和获取当前登陆用户id
 * @Version 1.0
 **/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
    * @Description: 设置值
    * @Param: [id]
    * @return: void
    * @Author: liyunzhi
    * @Date: 2022/6/24
    */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    /**
    * @Description: 获取值
    * @Param: []
    * @return: java.lang.Long
    * @Author: liyunzhi
    * @Date: 2022/6/24
    */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
