package com.action;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.util.ClassUtil;
import com.util.DateUtil;
import com.util.LoggerUtil;
import com.util.WebUtil;

public abstract class BaseAction {

    protected final static Logger log = LoggerUtil.getLogger();

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected static int PAGE_SIZE = 20;

    public void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    protected final void forward(String path) throws ServletException, IOException {
        log.info("view jsp:" + path);
        WebUtil.forward(request, response, path);
    }

    protected final void forwardLogin() throws ServletException, IOException {
        String loginUrl = request.getContextPath() + "/customer/index";
        response.getWriter().write("<script>top.document.location.href='" + loginUrl + "'</script>");
    }

    protected boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    protected int getPageNum(String pageNumStr) {
        int pageNum = ClassUtil.obj2int(pageNumStr);
        if (pageNum < 1) {
            pageNum = 1;
        } else if (pageNum > 100) {// 为了性能，最多只允许查询前100页
            pageNum = 100;
        }
        return pageNum;
    }

    protected <T> T getModel(Class<T> modelType) {
        return ClassUtil.getModel(request, modelType);
    }

    /**
     * 将request的参数设置到页面中
     * 
     * @author 玄承勇
     * @date 2014-5-24 上午11:40:37
     */
    protected void setAttr() {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            request.setAttribute(name, request.getParameter(name));
        }
    }

    protected boolean isEmity(String s) {
        return s == null || s.isEmpty();
    }

    protected int getInt(String name) {
        return ClassUtil.obj2int(request.getParameter(name));
    }

    protected long getLong(String name) {
        return ClassUtil.obj2long(request.getParameter(name));
    }

    protected String getString(String name) {
        return request.getParameter(name);
    }

    protected double getDouble(String name) {
        return ClassUtil.obj2doulbe(request.getParameter(name));
    }

    protected Date getDate(String name) {
        String dateStr = getString(name);
        if (!isEmpty(dateStr)) {
            return DateUtil.str2date(dateStr);
        }
        return null;
    }

    protected Date getDate(String name, int addNum, int addUnit) {
        Date date = getDate(name);
        if (date != null) {
            return DateUtil.addDate(date, addNum, addUnit);
        }
        return null;
    }

}
