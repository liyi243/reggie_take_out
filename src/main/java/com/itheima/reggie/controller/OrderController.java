package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;

import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number){
        Page orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        orderLambdaQueryWrapper.like(number !=null,Orders::getNumber,number);
        orderLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(orderPage,orderLambdaQueryWrapper);
        return R.success(orderPage);
    }
}
