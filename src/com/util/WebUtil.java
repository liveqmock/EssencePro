package com.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.annotation.Annotation.NoFilterHtmlTag;
import com.model.ActionParamsModel;
import com.util.ip.IPLocation;
import com.util.ip.IPUtil;

public class WebUtil {

    private final static Logger log = LoggerUtil.getLogger();

    private final static String SESSION_AUTH_CODE = "key-authCode";
    private final static String KEY_ACTION_PARAMS_MODEL = "key-actionParamsModel";
    private final static String KEY_URI_KEY = "key-uriKey";
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public static void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        log.info("view path:" + path);
        request.getRequestDispatcher("/WEB-INF/view/" + path).forward(request, response);
    }

    /**
     * 根据request获取向前台传递的响应值。
     * 
     * @param request
     * @return
     * @author 玄承勇
     * @date 2014-5-14 下午4:42:26
     */
    public static Map<String, Object> getRequestInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            Object value = request.getAttribute(name);
            map.put(name, value);
        }
        return map;
    }

    /**
     * 
     * @description : 获取文件的绝对路径
     * @author : 玄承勇
     * @date: 2014-5-2 上午10:30:36
     * @param:
     * @param:
     * @return ： String
     */
    public static String getFilePath(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String realpath = session.getServletContext().getRealPath("/");
        realpath = realpath.substring(0, realpath.length() - 1);
        return realpath;
    }

    /**
     * 从Session中取出UserInfo
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */

    // 获得本机ip
    @SuppressWarnings("unused")
    public static String getip() {
        String lname = null;
        String lip = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            lip = address.getHostAddress();
            lname = address.getHostName();
            return lip;
        } catch (Exception e) {
            return lip;
        }
    }

    /**
     * 得到请求的Ip地址
     * 
     * @param request
     * @return
     * @author 玄承勇
     * @date 2014-5-20 下午5:33:01
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIpAddr2(HttpServletRequest request) {
        // 针对于nginx反向代理情况
        String ip = request.getHeader("X-Real-IP");
        // 针对于正常情况
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 通过机器名获取网卡地址
     * 
     * @param remotePcIP
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午8:56:37
     */
    public static String getMacAddressName(String remotePcIP) {
        String str = "";
        String macAddress = "";
        try {
            Process pp = Runtime.getRuntime().exec("nbtstat -a " + remotePcIP);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException ex) {
        }
        return macAddress;
    }

    /**
     * 拼jsp中的select-option的html
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午10:05:46
     * @param: value[]
     * @param: text[]
     * @param: selected
     */
    public static String HtmlSelect(String value[], String text[], String selected) {
        StringBuffer htmlSelect = new StringBuffer();
        try {
            for (int i = 0; i < text.length; i++) {
                if (selected != null && selected.trim().equals(value[i].trim())) {
                    htmlSelect.append("<OPTION value=\"");
                    htmlSelect.append(value[i]);
                    htmlSelect.append("\"  selected>");
                    htmlSelect.append(text[i]);
                    htmlSelect.append(" </OPTION>");
                } else {
                    htmlSelect.append("<OPTION value=\"");
                    htmlSelect.append(value[i]);
                    htmlSelect.append("\" >");
                    htmlSelect.append(text[i]);
                    htmlSelect.append(" </OPTION>");
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return htmlSelect.toString();
    }

    /**
     * 过滤html标签，获得纯文本内容
     * 
     * @param inputString
     * @return String
     */
    public static String htmlTagFilter(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
                                                                                                     // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
                                                                                                  // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE); // 启用不区分大小写的匹配
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }

    /**
     * 取得当前操作系统名称
     * 
     * @return
     */
    public static String getOsName() {
        String osname = "";
        osname = System.getProperty("os.name");
        return osname;
    }

    /**
     * 获取项目的绝对路径
     * 
     * @return
     * @author 玄承勇
     * @date 2014-5-20 下午5:33:24
     */
    public static String getProjectAbsolutePath() {
        String path = WebUtil.class.getResource("/").getPath();
        try {
            path = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("#获取项目绝对路径出错：转码失败！", e);
        }
        return path;
    }

    /**
     * 转换url地址为utf-8格式
     * 
     * @Title encodeURL
     * @author 玄承勇
     * @date 2013-7-29 上午11:37:44
     * @param path
     * @return String
     */
    public static String encodeURL(String path) {
        StringBuffer flag = new StringBuffer("");
        if (path == null || path.isEmpty()) {
            return flag.toString();
        }
        String vali = " +()";
        char[] valiarr = vali.toCharArray();
        try {
            char[] ch = path.toCharArray();

            outer: for (int i = 0; i < ch.length; i++) {
                if (StringUtil.isSingleByte(ch[i])) {
                    for (int j = 0; j < valiarr.length; j++) {
                        if (ch[i] == valiarr[j]) {
                            if (ch[i] == ' ') {
                                flag.append("%20");
                            } else {
                                flag.append(URLEncoder.encode(String.valueOf(ch[i]), "UTF-8"));
                            }
                            continue outer;
                        }
                    }
                    flag.append(ch[i]);
                } else {
                    flag.append(URLEncoder.encode(String.valueOf(ch[i]), "UTF-8"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            log.error("不支持的字符集 UTF-8", e);
        }
        return flag.toString();
    }

    public static String base64EncodeURL(String urls) throws IOException {
        URL url = new URL(encodeURL(urls));
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        while ((b = bis.read()) != -1) {
            baos.write(b);
        }
        return Base64.encodeBase64String(baos.toByteArray());
    }

    public static void setAuthCode(HttpServletRequest request, String authCodeStr) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_AUTH_CODE, authCodeStr);
    }

    /**
     * 重定向
     * 
     * @param res
     * @param path
     * @throws IOException
     * @author 玄承勇
     * @date 2014-7-1 上午8:40:42
     */
    public static void send301Redirect(HttpServletResponse res, String path) throws IOException {
        res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        res.setHeader("Location", path);
    }

    /**
     * 把请求中的键值对转换为指定类型的model
     * 
     * @param request
     * @param modelType
     * @return
     * @author 玄承勇
     * @date 2014-7-1 上午8:42:22
     */
    public static <T> T getModel(HttpServletRequest request, Class<T> modelType) {
        try {
            Map<String, Field> fieldMap = ModelUtil.getFieldMap(modelType);
            T t = modelType.newInstance();
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = request.getParameter(name);
                Field field = fieldMap.get(name.toLowerCase());
                if (field != null && value != null) {
                    field.setAccessible(true);
                    try {
                        if (field.getType().equals(String.class)) {// 过滤html标签
                            NoFilterHtmlTag noFilterHtmlTag = field.getAnnotation(NoFilterHtmlTag.class);
                            if (noFilterHtmlTag == null || !noFilterHtmlTag.value()) {
                                value = StringUtil.escapeHtml(value);
                            }
                        }
                        field.set(t, ClassUtil.obj2T(value, field.getType()));
                    } catch (Exception e) {
                        log.warn("form转换出错：fieldName:“" + name + "”，fieldValue:“" + value + "”");
                    }
                }
            }
            return t;
        } catch (Exception e) {
            log.error("创建model发生错误,uri:" + request.getRequestURI(), e);
            return null;
        }
    }

    public static String getUriKey(HttpServletRequest request) {
        return (String) request.getAttribute(KEY_URI_KEY);
    }

    public static void setUriKey(HttpServletRequest request, String uriKey) {
        request.setAttribute(KEY_URI_KEY, uriKey);
    }

    public static ActionParamsModel getActionParamsModel(HttpServletRequest request) {
        return (ActionParamsModel) request.getAttribute(KEY_ACTION_PARAMS_MODEL);
    }

    public static void setActionParamsModel(HttpServletRequest request, ActionParamsModel actionModel) {
        request.setAttribute(KEY_ACTION_PARAMS_MODEL, actionModel);
    }

    /**
     * 下载
     * 
     * @param downFileName
     * @param file
     * @param response
     * @return
     * @throws IOException
     * @author 玄承勇
     * @date 2014-7-1 上午8:51:42
     */
    public static boolean download(String downFileName, File file, HttpServletResponse response) throws IOException {
        if (file != null) {
            // 如果文件名没有扩展名，则根据路径添加
            if (!downFileName.contains(".")) {
                String filePath = file.getName();
                downFileName += filePath.substring(filePath.lastIndexOf("."));
            }
            /* 清空再设置header */
            response.reset();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(downFileName.getBytes("GBK"), "iso-8859-1")
                    + "\"");
            PrintWriter out = response.getWriter();
            FileInputStream in = new FileInputStream(file);
            try {
                int i;
                while ((i = in.read()) != -1) {
                    out.write(i);
                }
            } finally {
                in.close();
                out.close();
            }
            return true;
        }
        return false;
    }

    public static void sendJson(HttpServletResponse response, Object obj) throws IOException {
        response.getWriter().print(JSON.toJSONString(obj, SerializerFeature.BrowserCompatible));
    }

    // 验证验证码
    public static boolean checkAuthCode(HttpServletRequest request, String authCodeName) {
        return checkAuthCode(request, authCodeName, true);
    }

    // 验证验证码
    public static boolean checkAuthCode(HttpServletRequest request) {
        return checkAuthCode(request, "random", true);
    }

    // 验证验证码
    public static boolean checkAuthCode(HttpServletRequest request, boolean remove) {
        return checkAuthCode(request, "random", remove);
    }

    /**
     * 
     * 验证码的验证
     * 
     * @Title checkAuthCode
     * @date 2014-1-7 下午2:22:12
     * @param request
     * @param remove
     *            验证成功后是否移除
     * @return boolean
     */
    public static boolean checkAuthCode(HttpServletRequest request, String authCodeName, boolean remove) {
        boolean flag = false;
        try {
            HttpSession session = request.getSession();
            String authCode = (String) session.getAttribute(SESSION_AUTH_CODE);
            if (authCode == null) {
                log.warn("验证码过期");
                return false;
            }
            String randomIn = request.getParameter(authCodeName);
            if (randomIn != null && randomIn.toLowerCase().equals(authCode)) {
                if (remove) {
                    session.removeAttribute(SESSION_AUTH_CODE);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("===验证码验证出错==", e);
        }
        return flag;
    }

    public static JSONObject getJsonResult(Integer result, String msg, Map<String, Object> dataMap) {
        JSONObject jsonObj = new JSONObject();
        Map<String, Object> objMap = new HashMap<String, Object>();
        if (result != null) {
            objMap.put("result", result + "");
        }
        if (msg != null) {
            objMap.put("msg", msg);
        }
        if (dataMap != null) {
            objMap.putAll(dataMap);
        }
        jsonObj.putAll(objMap);
        return jsonObj;
    }

    public static JSONObject getJsonResult(Map<String, Object> dataMap) {
        return getJsonResult(null, null, dataMap);
    }

    public static JSONObject getJsonResult(Integer result, String msg) {
        return getJsonResult(result, msg, null);
    }

    public static void sendError(HttpServletResponse response, String message) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("err", true);
        obj.put("message", message);
        sendJson(response, obj);
    }

    public static void sendOk(HttpServletResponse response, String message) throws IOException {
        sendOk(response, message, null, null);
    }

    public static void sendOk(HttpServletResponse response, String message, Object value) throws IOException {
        sendOk(response, message, value, null);
    }

    public static void sendOk(HttpServletResponse response, String message, Object value, Object refresh) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("err", false);
        obj.put("message", message);
        if (value != null) {
            obj.put("value", value);
        }
        if (refresh != null) {
            obj.put("refresh", refresh);
        }
        sendJson(response, obj);
    }

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

    public static IPLocation getIpArea(String ip) {
        IPLocation location = null;
        if (ip == null)
            return null;
        if (ip != null) {
            IPUtil ipseeker = IPUtil.getInstance();
            location = ipseeker.getIPLocation(ip);
        }
        return location;
    }

    // 获取服务器的机器名
    public static String getMachineName() {
        String machineName = "";
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            machineName = addr.getHostName().toString();
        } catch (UnknownHostException e) {
            log.error(e);
        }

        return machineName;
    }

    // 获取项目所在的目录
    public static String getProjectDir() {
        String projectDir = "";
        projectDir = System.getProperty("user.dir").replace("bin", "");
        return projectDir;
    }

    /**
     * 
     * 添加浏览器缓存
     * 
     * @Title addBrowerCache
     * @author 玄承勇
     * @date 2014-5-21 上午8:36:53
     * @param expiresSeconds
     *            缓存时间
     * @param response
     *            void
     */
    public static void addBrowerCache(int expiresSeconds, HttpServletResponse response) {
        Date createDate = new Date();
        response.setHeader("Cache-Control", "public, max-age=" + expiresSeconds);
        String gmtDateStr = DateUtil.getNowIeGMT(createDate);
        response.setHeader("Last-Modified", gmtDateStr);
        response.setHeader("date", gmtDateStr);
    }

    /**
     * 
     * 缓存是否可用
     * 
     * @Title canUseBrowerCache
     * @author 玄承勇
     * @date 2014-5-21 上午8:35:06
     * @param expiresSeconds
     *            缓存时间
     * @param request
     * @return boolean
     */
    public static boolean canUseBrowerCache(int expiresSeconds, HttpServletRequest request) {
        String lastModifyDate = (String) request.getHeader("If-Modified-Since");
        Date lastMdate = null;
        if (lastModifyDate != null) {
            lastMdate = DateUtil.gmt2Date(lastModifyDate);
        }
        if (lastMdate != null && System.currentTimeMillis() <= lastMdate.getTime() + expiresSeconds) {
            return true;
        }
        return false;
    }

    /**
     * 每次请求都会得到一个新的会话
     * 
     * @param url
     * @return
     * @author 玄承勇
     * @date 2014-7-2 上午8:39:13
     */
    public static Map<String, String> getCookiesMap(String url) {
        Map<String, String> mapcookies = new HashMap<String, String>();
        for (int k = 0; k < 3; k++) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Referer", url);
            httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET4.0C; .NET4.0E");
            try {
                httpClient.execute(httpGet);
                List<org.apache.http.cookie.Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
                if (cookies.isEmpty()) {
                    return null;
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        mapcookies.put(cookies.get(i).getName(), cookies.get(i).getValue());
                    }
                    return mapcookies;
                }

            } catch (ClientProtocolException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                /* 终止程序 */
                httpGet.abort();
                httpGet.releaseConnection();
            }
        }
        return mapcookies;
    }

}
