package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

public class PathUtil {

    private final static Logger log = LoggerUtil.getLogger();

    public static String getProjectAbsolutePath() {
        String path = LoggerUtil.class.getResource("/").getPath();
        try {
            path = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("#获取项目绝对路径出错：转码失败！", e);
        }
        return path;
    }

}
