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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
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
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
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

    /**
     * 转换url地址为utf-8格式
     * 
     * @Title encodeURL
     * @author 吕凯
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
}
