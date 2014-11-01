package com.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model.ConfigModel;
import com.service.SecurityService;

public class CookieUtil {
    /**
     * 添加cookie。包含域名domain
     * 
     * @param response
     * @param key
     * @param value
     * @author 玄承勇
     * @date 2014-6-27 下午3:42:00
     */
    public static void addCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(ClassUtil.obj2int(ConfigModel.CONFIG.getProperty("cookie.timout", "1800")));// 有效期30分钟
        cookie.setDomain(ConfigModel.CONFIG.getProperty("cookie.domain"));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 添加cookie,不包含域名
     * 
     * @param response
     * @param key
     * @param value
     * @author 玄承勇
     * @date 2014-6-27 下午3:42:38
     */
    public static void addNormalCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(1800);// 有效期30分钟
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     * 
     * @param response
     * @param key
     * @author 玄承勇
     * @date 2014-6-27 下午3:42:59
     */
    public static void removeNormalCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即删除型
        response.addCookie(cookie);
    }

    /**
     * 删除cookie及域名
     * 
     * @param response
     * @param key
     * @author 玄承勇
     * @date 2014-6-27 下午3:43:13
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即删除型
        cookie.setDomain(ConfigModel.CONFIG.getProperty("cookie.domain"));
        response.addCookie(cookie);
    }

    /**
     * 清空cookie
     * 
     * @param request
     * @param response
     * @author 玄承勇
     * @date 2014-6-27 下午3:44:18
     */
    public static void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setDomain(ConfigModel.CONFIG.getProperty("cookie.domain"));
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * 得到cookie
     * 
     * @param request
     * @param key
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午3:44:43
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 单点登陆可用时，根据用户信息生成ticket,并写入到cookie中。
     * 
     * @param response
     * @param loginname
     * @param password
     * @param userType
     * @param isManage
     * @param userId
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-27 下午4:25:04
     */
    public static void addToken2Cookie(HttpServletResponse response, String loginname, String password, String userType, String isManage,
            String userId) throws Exception {
        if (useSSO()) {
            String dateUtil = DateUtil.getDateTimeString(DateUtil.addDate(new Date(), Calendar.DAY_OF_MONTH, 1));
            String s = loginname + "\n" + password + "\n" + userType + "\n" + isManage + "\n" + userId + "\n" + dateUtil;
            String token = SecurityService.encryptOut(s);
            addCookie(response, "token", token);
        }
    }

    /**
     * 从cookie中移除ticket
     * 
     * @param response
     * @author 玄承勇
     * @date 2014-6-27 下午4:28:00
     */
    public static void removeToken4Cookie(HttpServletResponse response) {
        if (useSSO()) {
            removeCookie(response, "token");
        }
    }

    /**
     * 获取ticket
     * 
     * @param request
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午4:28:53
     */
    public static String getToken4Cookie(HttpServletRequest request) {
        return getCookie(request, "token");
    }

    /**
     * 更新ticket
     * 
     * @param request
     * @param response
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午4:30:08
     */
    public static boolean updToken4Cookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    cookie.setPath("/");
                    cookie.setMaxAge(ClassUtil.obj2int(ConfigModel.CONFIG.getProperty("cookie.timout", "1800")));// 有效期30分钟
                    cookie.setDomain(ConfigModel.CONFIG.getProperty("cookie.domain"));
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从cookie中获取有效的ticket如果过期，则返回null。
     * 
     * @param tokenEncry
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-27 下午4:39:02
     */
    public static String[] getTokenArrfromCookie(String tokenEncry) throws Exception {
        String token = SecurityService.decryptOut(tokenEncry);
        String[] tokens = token.split("\n");
        if (tokens != null) {
            String dateUtilStr;
            try {
                dateUtilStr = tokens[5];
            } catch (Exception e) {
                return null;
            }
            Date dateUtil = DateUtil.str2dateTime(dateUtilStr);
            // 判断获取的token是否在有效期内。
            if (dateUtil == null || new Date().after(DateUtil.addDateByMinute(dateUtil, 30))) {
                return null;
            }
        }
        return tokens;
    }

    public static boolean useSSO() {
        boolean flag = false;
        String use = ConfigModel.CONFIG.getProperty("cookie.usesso", "false");
        try {
            flag = Boolean.parseBoolean(use);
        } catch (Exception e) {
        }
        return flag;
    }
}
