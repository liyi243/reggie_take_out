package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");

    }
    @GetMapping("/page")
    public R<Page>page(int page, int pageSize, String name){
        //构造分页对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        dishLambdaQueryWrapper.like(name != null,Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,dishLambdaQueryWrapper);
        //return R.success(pageInfo);

        //对象拷贝:将pageInfo的属性对应拷贝到dishDtoPage，但不拷贝records属性
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();//获取当前页数据
        List<DishDto> list = records.stream().map((item) -> {
            //item是每一个Dish对象
            DishDto dishDto = new DishDto();
            //拷贝dish普通属性到dishDto
            BeanUtils.copyProperties(item,dishDto);

            //拿到分类id去查分类表，得到分类名称
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象,给到dishDto
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    //根据id查询菜品
    @GetMapping("/{id}")
    public R<DishDto>get(@PathVariable Long id){
        log.info("根据id查询菜品...id{}",id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    //修改菜品
    @PutMapping
    public R<String>update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
    * @Description: 新建套餐中"添加菜品"的菜品查询
    * @Param: [dish]
    * @return: com.itheima.reggie.common.R<java.util.List<com.itheima.reggie.entity.Dish>>
    * @Author: liyunzhi
    * @Date: 2022/6/27
    */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1(起售状态)的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return R.success(list);
    }

    //菜品停售
    /**
     * 根据id(批量)停售/启售套餐信息
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateMulStatus(@PathVariable Integer status, Long[] ids){
        List<Long> list = Arrays.asList(ids);
        //构造条件构造器
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤条件
        updateWrapper.set(Dish::getStatus,status).in(Dish::getId,list);
        dishService.update(updateWrapper);

        return R.success("菜品信息修改成功");
    }
    //批量单个删除
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("菜品id{}",ids);
        dishService.removeDish(ids);
        return R.success("菜品删除成功");
    }
}
