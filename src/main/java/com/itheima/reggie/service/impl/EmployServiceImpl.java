package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
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
public class EmployServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
