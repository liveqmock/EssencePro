package com.web;

import javax.servlet.http.HttpServletRequest;

public class FilePath {

    // 获取文件的绝对路径和相对路径
    public String getFilePath(HttpServletRequest request, String type) {
        // 获取协议，例如：http://
        request.getScheme();
        // 获得服务器名称,例如127.0.0.1
        request.getServerName();
        // 获得端口号比如8080
        request.getServerPort();
        // 获得当前上下文的路径，
        request.getContextPath();
        // 获得文件的绝对路径——//E:\tomcat\webapps\myajax\serinfo.properties
        String absolutePath = request.getSession().getServletContext().getRealPath("/") + "serinfo.properties";
        // 获得文件的相对路径——/myajax/serinfo.properties
        String relativePath = request.getContextPath();
        if (type.equals("1")) {
            return absolutePath;
        } else if (type.equals("2")) {
            return relativePath;
        } else {
            return "";
        }

    }
}
