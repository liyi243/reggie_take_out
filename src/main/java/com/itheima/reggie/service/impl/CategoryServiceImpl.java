package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Service
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 扩展方法（自定义）,来处理菜品、套餐和分类绑定，删除分类的异常
     * 根据id删除分类，删除前需要做判断
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据分类id查询菜品数据
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //记查询结果
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查到数据说明关联，会抛出业务异常
        if(count1 > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据分类id查询菜品数据
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //记查询结果
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查到数据说明关联，会抛出业务异常
        if(count2 > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除:可以走到这里，就说明没有关联菜品或套餐
        super.removeById(id);
    }
}
