package com.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model.StaticModel;
import com.model.User;

public class PowerHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    // 先补充HandlerInterceptorAdapter的相关知识，下面三个方法为HandlerInterceptorAdapter的方法。
    // spring MVC拦截器不仅实现了Filter的全部功能，还可以精确的控制拦截精度，有三个方法。

    // 1、预处理——可以进行编码、安全控制
    /*
     * public boolean preHandle(HttpServletRequest request, HttpServletResponse
     * response, Object handler) throws Exception { return true; }
     */
    // 2、后处理——调用Service并返回ModelAndView，但未进行页面渲染
    /*
     * public void postHandle(HttpServletRequest request, HttpServletResponse
     * response, Object handler, ModelAndView modelAndView) throws Exception { }
     */
    // 3、返回处理——已经渲染页面。可以根据ex是否为null判断是否发生了异常，进行日志记录
    /*
     * public void afterCompletion(HttpServletRequest request,
     * HttpServletResponse response, Object handler, Exception ex) throws
     * Exception { }
     */
    /**
     * Intercepter实现权限拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        // 不需要访问权限的url
        if (path.matches(StaticModel.NO_INTERCEPTER_PATH)) {
            return Boolean.TRUE;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(StaticModel.SESSION_USER);
        response.setContentType("text/html;charset=utf-8");
        // 判断user是否为空，空意味着没登陆或session已过期，跳转到登陆界面
        if (null == user) {
            response.getWriter().write("请登陆！");
            return false;
        }

        // 判断是否是超级管理员
        if (isSuper(user)) {
            return Boolean.TRUE;
        }

        // 判断是否有权限。
        if (hasRole(path, user)) {
            // 记录操作日志.
            writeLog(path, user);
            return Boolean.TRUE;
        } else {
            response.getWriter().write("此用户无权限");
            return false;
        }
    }

    // 判断是否是超级管理员
    private boolean isSuper(User user) {
        return true;
    }

    // 写入操作日志
    private void writeLog(String path, User user) {
    }

    // 判断是否有权限
    private boolean hasRole(String path, User user) {

        path = path.substring(1);
        String funcPart = path.substring(path.lastIndexOf("/") + 1);
        // url结尾替换为manage.do。实现用户有此类的manage.do权限时，可以访问此类的所有方法。
        String url = path.replace(funcPart, "manage.do");
        boolean flag = false;
        // 查询出此用户的所有权限，判断
        /*
         * List<XtGgFunctionMenu> menuList = czy.getRole().getFuncList();
         * for(XtGgFunctionMenu menu : menuList){
         * if(url.equals(menu.getTheUrl())){ flag = Boolean.TRUE; break; } }
         */
        return flag;
    }
}
