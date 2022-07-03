package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //不用写HttpServletRequest request,之前写这个是为了从session中获取，现在不需要了
    @PostMapping
    public R<String> save( @RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();

        qw.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,qw);
        return R.success(pageInfo);
    }
    // @GetMapping("/{id}")
    // public R<Category> getById(@PathVariable Long id){
    //     log.info("根据id查询菜品信息...id:{}",id);
    //     Category category = categoryService.getById(id);
    //     if(category != null){
    //         return R.success(category);
    //     }
    //     return R.error("没有查询到菜品信息");
    // }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息",category);
        categoryService.updateById(category);
        return R.success("菜品修改成功");
    }
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类:{}",id);
        //categoryService.removeById(id);//关联菜品的分类会直接删除，出现业务逻辑异常
        categoryService.remove(id);
        return R.success("删除菜品成功");
    }

    /**
     * @Description 菜品管理功能新增菜品后的菜品分类框框显示已有菜品
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() !=null,Category::getType,category.getType());
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }

}
