package com.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessFilter implements Filter {
    private static Map<String, String> userMap = new HashMap<String, String>();

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        System.out.println("被filter拦截了");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // request中特殊字符转义
        // Iterator it = request.getParameterMap().values().iterator();
        // while (it.hasNext()) {
        // String[] params = (String[]) it.next();
        //
        // for (int i = 0; i < params.length; i++) {
        // params[i] = params[i].replaceAll("<script>", "<_script>");
        // params[i] = params[i].replaceAll("<", "&lt;");
        // params[i] = params[i].replaceAll(">", "&gt;");
        // params[i] = params[i].replaceAll("\r", "");
        // params[i] = params[i].replaceAll("\n", "");
        // }
        // }
        // 得到的字符串为端口号后面的url
        String url = request.getRequestURI();

        // 判断是否从登陆界面访问，不是，跳转到登陆界面。
        // if (url.equals("/EssenceProject/")) {
        // chain.doFilter(request, response);
        // request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request,
        // response);

        // return;
        // }
        // 是从登陆界面进行访问，判断用户名和密码是否正确，是时，跳转到要访问的资源。
        // if (url.equals("/EssenceProject/login.do")) {
        // String username = request.getParameter("username");
        // String password = request.getParameter("userpwd");
        // for (Entry<String, String> entry : userMap.entrySet()) {
        // if ((entry.getKey().equals(username)) &&
        // (entry.getValue().equals(password))) {
        // System.out.println("用户名和密码匹配正确");
        // //
        // request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request,
        // // response);
        // chain.doFilter(request, response);
        // return;
        // }
        // }
        // request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request,
        // response);
        // // response.getWriter().print("用户名或密码错误");
        // }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig config) throws ServletException {
        userMap.put("user1", "1");
        userMap.put("user2", "2");
    }

    public void destroy() {
        System.out.println("filter  销户了");
    }
}
