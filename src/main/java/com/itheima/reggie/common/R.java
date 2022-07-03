package com.itheima.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName 通用结果类
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Data
public class R<T> {
    private  Integer code; //1成功0失败
    private String msg;
    private T data;
    private Map map = new HashMap();

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}

