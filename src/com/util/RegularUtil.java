package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

public class RegularUtil {

    public static Logger log = LoggerUtil.getLogger();

    public static String getStringByPatten(String subjectString, String strreg) {
        try {
            Pattern regex = Pattern.compile(strreg);
            Matcher regexMatcher = regex.matcher(subjectString);
            if (regexMatcher.find()) {
                return regexMatcher.group(1);
            }
        } catch (PatternSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static int getPageByPatten(String source, String pattern) {
        // String subjectString = "<sp共an> 共33页 到</span><input 页type=\"text\" ";
        try {
            Pattern regex = Pattern.compile(pattern);// 共(\\d.)页
            Matcher regexMatcher = regex.matcher(source);
            while (regexMatcher.find()) {
                return Integer.parseInt(regexMatcher.group(1));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    public static String getPageStringByPatten(String source, String pattern) {
        // String subjectString = "<sp共an> 共33页 到</span><input 页type=\"text\" ";
        try {
            Pattern regex = Pattern.compile(pattern);// 共(\\d.)页
            Matcher regexMatcher = regex.matcher(source);
            while (regexMatcher.find()) {
                return StringUtil.getOnNum(regexMatcher.group(1));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "0";
    }

}
