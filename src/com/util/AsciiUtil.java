package com.util;

public class AsciiUtil {
    /**
     * ascii码转换为字符串
     * 
     * @param asciiStr
     * @return
     * @author 玄承勇
     * @date 2014-10-13 下午4:36:08
     */
    public static String asciiToStr(String asciiStr) {
        if (asciiStr == null) {
            return "";
        }

        StringBuilder result = new StringBuilder("");
        String[] asciiArr = asciiStr.split(",");
        for (int i = 0; i < asciiArr.length; i++) {
            char tmp = (char) Integer.parseInt(asciiArr[i]);
            result.append(tmp);
        }

        return result.toString();
    }

    /**
     * 字符串转换为ascii码
     * 
     * @param str
     * @return
     * @author 玄承勇
     * @date 2014-10-13 下午4:34:22
     */
    public static String strToAscii(String str) {
        if (str == null) {
            return "";
        }

        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            int ascii = (int) tmp;
            if (i < (str.length() - 1)) {
                result.append(ascii).append(",");
            } else {
                result.append(ascii);
            }
        }

        return result.toString();
    }
}
