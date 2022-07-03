package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;

public interface CategoryService extends IService<Category> {
    //扩展方法（自定义）,来处理菜品和分类绑定，删除分类的异常
    public void remove(Long id);
}
