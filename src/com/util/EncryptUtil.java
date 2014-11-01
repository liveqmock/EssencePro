/*
 * Copyright (c) 2013 zljysoft. 中联佳裕信息技术（北京）有限公司 版权所有 
 * http://www.zljysoft.com
 * File Name：EncryptUtil.java
 * Comments: 支持SHA/MD5消息摘要的工具类
 * Create Date: 2013-4-10 上午10:04:41
 * Modified By: 
 * Modified Date: 
 * Why & What is modified: 
 * version: V1.0 
 */
package com.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 支持SHA/MD5消息摘要的工具类
 * 
 * @date 2013-4-10 上午10:04:41
 * @version V1.0
 * 
 */
public class EncryptUtil {

    public static Logger log = LoggerUtil.getLogger();

    /**
     * 返回MD5码
     * 
     * @param parStr
     *            要编码的字符串
     * @return 返回值
     */
    public static String getMD5Code(String parStr) {
        return DigestUtils.md5Hex(parStr);
    }

    /**
     * 返回字节数组MD5码 例如:可用于返回文件的校验码
     * 
     * @param parByteArray
     *            字节数组
     * @return 返回值
     */
    public static String getMD5Code(byte[] parByteArray) {
        return DigestUtils.md5Hex(parByteArray);
    }

    /**
     * 返回SHA码
     * 
     * @param parStr
     *            要编码的字符串
     * @return 返回值
     */
    public static String getSHACode(String parStr) {
        return DigestUtils.shaHex(parStr);
    }

    /**
     * 返回字节数组SHA码 例如:可用于返回文件的校验码
     * 
     * @param parByteArray
     *            字节数组
     * @return 返回值
     */
    public static String getSHACode(byte[] parByteArray) {
        return DigestUtils.shaHex(parByteArray);
    }

    /**
     * MD5加密方法
     * 
     * @param s
     * @return
     */
    public final static String MD5(String s) {
        if (s == null)
            return null;
        char hexDigits[] = { 'D', 'E', 'F', '0', 'A', 'B', 'C', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String base64Encode(String str) {
        if (null == str || "".equals(str)) {
            return null;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(str.getBytes());
    }

    public static String base64Decode(String str) {
        if (null == str || "".equals(str)) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha1Encode(String str) {
        return DigestUtils.sha1Hex(str);
    }

    public static String encodeUrl(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String encodeUrlGB2312(String str) {
        try {
            return URLEncoder.encode(str, "gb2312");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(base64Encode("重庆金创商务有限公司"));
    }
}
