package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName 登陆校验过滤器
 * @Description
 * @Author:liyunzhi
 * @Date
 * @Version 1.0
 **/
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的url
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**",
                "/front/**"
        };

        //判断本次请求是否需要处理
        boolean check = check(urls,requestURI);
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登陆状态，如果已登陆，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登陆,用户id为:{}",request.getSession().getAttribute("employee"));

            //将当前登陆用户id存到ThreadLocal
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            //放行
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登陆");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    /**
    * @Description: 路径匹配，检查本次请求是否需要放行
    * @Param: [urls, requestURI]
    * @return: boolean
    * @Author: liyunzhi
    * @Date: 2022/6/23
    */
    public boolean check(String[] urls,String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
