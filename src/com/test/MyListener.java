package com.test;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyListener implements HttpSessionListener, ServletContextListener, ServletContextAttributeListener {

    @Override
    public void attributeAdded(ServletContextAttributeEvent arg0) {

    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent arg0) {

    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent arg0) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

}
