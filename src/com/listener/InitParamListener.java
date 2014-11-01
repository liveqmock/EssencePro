package com.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.model.StaticModel;
import com.service.UserService;

public class InitParamListener implements HttpSessionListener,
		ServletContextListener, ServletContextAttributeListener {

	private ServletContext context = null;

	private UserService userService = null;

	public void attributeAdded(ServletContextAttributeEvent arg0) {
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	// context初始化时会激发
	public void contextInitialized(ServletContextEvent sce) {
		// 封装ApplicationContext,用于根据名字得到某个bean.
		StaticModel.WEB_APP_CONTEXT = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());
		this.context = sce.getServletContext();
		// 初始化一些参数，例如访问权限路径。
		initParam(context);
	}

	public void sessionCreated(HttpSessionEvent arg0) {
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
	}

	public void initParam(ServletContext context) {
		// 得到UserService的bean.
		// userService = (UserService)
		userService = (UserService) StaticModel.WEB_APP_CONTEXT
				.getBean("userService");
	}

}
