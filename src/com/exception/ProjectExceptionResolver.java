package com.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//捕获异常，前台界面显示异常信息。
public class ProjectExceptionResolver implements HandlerExceptionResolver {

    public ProjectExceptionResolver() {
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("捕获异常，异常开始===========================================");
        ex.printStackTrace();

        ModelAndView mv = new ModelAndView("errorException");
        mv.addObject("title", "捕获异常，出错了");
        mv.addObject("exception", ex.toString());
        System.out.println("异常结束=================================================。");

        return null;
    }

}
