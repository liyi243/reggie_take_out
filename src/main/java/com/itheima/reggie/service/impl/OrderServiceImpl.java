package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.OrderService;


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
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Orders>implements OrderService {
}
