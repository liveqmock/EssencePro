package com.model;

import org.springframework.context.ApplicationContext;

public class StaticModel {

    // 权限
    public static final String NO_INTERCEPTER_PATH = "login|logout"; // 不需要访问权限的url，从数据库中查出。
    public static final String INTERCEPTER_PATH = "*********"; // 需要访问权限才能访问的url，从数据库中查出。

    // 该值会在web容器启动时由WebAppContextListener初始化
    public static ApplicationContext WEB_APP_CONTEXT = null;

    // session
    public static String SESSION_USER = "sessionUser";

}
