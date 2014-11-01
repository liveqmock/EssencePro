package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 电话相关工具类
 * 
 * @ClassName PhoneUtil
 * @author 玄承勇
 * @date 2014-5-21 下午17:19:41
 * 
 */
public class PhoneUtil {
    private final static Logger log = LoggerUtil.getLogger();
    // 用于匹配固定电话号码
    private final static String REGEX_FIXEDPHONE = "^((010|02\\d|0[3-9]\\d{2})-?)?\\d{6,8}$";
    private final static Pattern PATTERN_ZIPCODE = Pattern.compile(REGEX_FIXEDPHONE);

    /**
     * 获取手机号码信息
     * 
     * @param phoneNum
     * @return
     * @author 玄承勇
     * @date 2014-5-21 下午5:20:03
     */
    public static String getMobileCodeInfo(String phoneNum) {
        HttpClient httpclient = new DefaultHttpClient();
        Map<String, String> map = new HashMap<String, String>();// 参数
        map.put("mobileCode", phoneNum);
        map.put("userID", "");
        String result = "";
        try {
            result = returnMobileCodeInfo(httpclient, "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo",
                    map);// 登录
        } catch (Exception e) {
            log.error("获取电话归属地出错" + phoneNum, e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }

    private static String returnMobileCodeInfo(HttpClient httpclient, String url, Map<String, String> map) throws Exception {
        String flag = "未知区域";
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            formparams.add(new BasicNameValuePair(key, map.get(key)));
        }
        UrlEncodedFormEntity uefEntity;

        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(uefEntity);
        HttpResponse response;
        response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String returnContent = EntityUtils.toString(entity, "UTF-8");
            returnContent = returnContent.replaceAll("<\\?xml.*\\?>", "");

            Document document = DocumentHelper.parseText(returnContent);
            Element root = document.getRootElement();
            if (root != null) {
                String phoneNum = map.get("mobileCode");
                flag = (root.getText() + "").replace(phoneNum + "：", "").trim();
            }
        }
        return flag.replaceAll("http://.*", "");
    }

    /**
     * 获取固定电话号码信息
     * 
     * @param phoneNum
     * @return
     * @author 玄承勇
     * @date 2014-5-21 下午5:20:19
     */
    public static String getMobileCodeInfo138(String phoneNum) {
        String result = "未知区域";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            result = returnMobileCodeInfo138(httpclient, "http://www.ip138.com:8080/search.asp?action=mobile&mobile=" + phoneNum);// 登录
        } catch (Exception e) {
            log.error("获取手机号归属地出错" + phoneNum, e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }

    private static String returnMobileCodeInfo138(HttpClient httpclient, String url) throws Exception {
        String flag = "未知地区";
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String returnContent = EntityUtils.toString(entity, "GBK").toLowerCase();
            if (returnContent.contains("手机号有误")) {
                return flag = "手机号有误";
            }
            returnContent = returnContent.replaceAll("<!--.*?-->", "");
            Pattern pattern = Pattern.compile(".*卡号归属地.*?<td.*?>(.*?)</td>.*卡.*?类.*?型.*?<td.*?>(.*?)</td>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(returnContent);
            if (matcher.find()) {
                flag = matcher.group(1) + matcher.group(2);
                if (flag.matches("<a.*>更详细的.*</a>")) {
                    flag = "未知地区";
                }
            }
        }
        return flag.replaceAll("<a.*?>.*?</a>|&nbsp;", "");
    }

    /**
     * 获取固定电话号码信息
     * 
     * @param phoneNum
     * @return
     * @author 玄承勇
     * @date 2014-5-21 下午5:20:30
     */
    public static String getTelPhoneCodeInfo(String phoneNum) {
        String result = "未知区域";
        if (phoneNum.startsWith("86-")) {
            phoneNum = phoneNum.substring(3);
        } else if (phoneNum.startsWith("86")) {
            phoneNum = phoneNum.substring(2);
        }
        if (!phoneNum.startsWith("0")) {
            phoneNum = "0" + phoneNum;
        }
        if (phoneNum.matches(REGEX_FIXEDPHONE)) {
            Matcher matcher = PATTERN_ZIPCODE.matcher(phoneNum);
            if (matcher.find()) {
                String zipcode = matcher.group(2);
                HttpClient httpclient = new DefaultHttpClient();
                try {
                    result = returnTelPhoneCodeInfo(httpclient, "http://www.ip138.com/post/search.asp?action=zone2area&zone=" + zipcode);// 登录
                } catch (Exception e) {
                    log.error("获取电话归属地出错" + phoneNum, e);
                } finally {
                    httpclient.getConnectionManager().shutdown();
                }
                return result;
            }
        }
        return result;
    }

    private static String returnTelPhoneCodeInfo(HttpClient httpclient, String url) throws Exception {
        String flag = "未知地区";
        // 创建httppost
        HttpGet httpget = new HttpGet(url);

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String returnContent = EntityUtils.toString(entity, "GBK").toLowerCase();
            Pattern pattern = Pattern.compile("查询结果.*?<tr><td.*?>(.*?)</td></tr>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(returnContent);
            if (matcher.find()) {
                flag = matcher.group(1);
                if (flag.matches("<a.*>更详细的.*</a>")) {
                    flag = "未知地区";
                }
            }
        }
        return flag.replaceAll("邮编.*|◎", "").replaceAll("&nbsp;|◎", "");
    }

    /**
     * 获取电话号码信息
     * 
     * @param phoneNum
     * @return
     * @author 玄承勇
     * @date 2014-5-21 下午5:20:41
     */
    public static String getPhoneCodeInfo(String phoneNum) {
        String result = "非法号码";
        if (phoneNum == null)
            return result;
        if (phoneNum.matches(REGEX_FIXEDPHONE) || phoneNum.length() < 11 || phoneNum.contains("-")) {
            result = getTelPhoneCodeInfo(phoneNum);
        } else if (phoneNum.matches("\\+?\\d{11,}")) {
            result = getMobileCodeInfo138(phoneNum);
        }
        return result;
    }

}
