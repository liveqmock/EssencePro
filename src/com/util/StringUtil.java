package com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class StringUtil {

    public static Logger log = Logger.getLogger(StringUtil.class);

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    // 替换字符串line中的oldString为newString.
    public static final String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    // 字符串str按照sign进行分割成数组
    public static String[] StringPartition(String str, String sign) {
        List<String> arrayString = new ArrayList<String>();
        if (str.indexOf(sign) != -1) {
            StringTokenizer st = new StringTokenizer(str, sign);
            while (st.hasMoreTokens()) {

                arrayString.add(st.nextToken());

            }
            return arrayString.toArray(new String[arrayString.size()]);
        } else {
            return null;
        }
    }

    /**
     * 以li分割str字符串，返回字符串数组
     */
    public static String[] explode(String li, String str) {
        if (str == null) {
            String[] rs = new String[0];
            return rs;
        }
        int num = 1;
        String temp = str;
        for (int i = temp.indexOf(li); i > -1; i = temp.indexOf(li)) {
            temp = temp.substring(i + li.length());
            num++;
        }
        // if (num == 1)
        // return new String[0];
        int j = 0;
        temp = str;
        String[] rs = new String[num];
        for (int i = 1; i < num; i++) {
            int p = temp.indexOf(li);
            rs[j] = temp.substring(0, p);
            temp = temp.substring(p + li.length());
            j++;
        }
        rs[j] = temp;
        return rs;
    }

    /**
     * 以li分割str字符串，返回字符串数组
     */
    public static String[] split(String li, String str) {
        if ((str == null) || (str.trim().length() == 0))
            str = null;
        return explode(li, str);
    }

    /**
     * 以li分割str字符串，返回字符串数组
     */
    public static String[] explode_new(String li, String str) {
        StringTokenizer st = new StringTokenizer(str, li);
        int rssize = 0;
        if (str.startsWith(li))
            rssize++;
        if (str.endsWith(li))
            rssize++;
        String[] rs = new String[st.countTokens() + rssize];
        int i = 0;
        if (str.startsWith(li)) {
            rs[i] = "";
            i++;
        }
        while (st.hasMoreTokens()) {
            rs[i] = st.nextToken();
            i++;
        }
        if (str.endsWith(li)) {
            rs[i] = "";
        }
        return rs;
    }

    /**
     * 将str字符串转换成数字
     */
    public static int StrToInt(String str) {
        int rs = 0;
        if (str != null) {
            try {
                Integer in = new Integer(str);
                rs = in.intValue();
            } catch (NumberFormatException e) {
                rs = 0;
            }
        }
        return rs;
    }

    /**
     * 
     * @description : string格式化为utf-8
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午10:36:29
     * @param:
     * @param:
     * @return ： String
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    // System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    public static String lowerCase(String str) {
        String tempstr = new String("");
        char tempch = ' ';
        for (int i = 0; i < str.length(); i++) {
            tempch = str.charAt(i);
            if (64 < str.charAt(i) && str.charAt(i) < 91)// 是大写字母
                tempch += 32;
            tempstr += tempch;
        }
        return tempstr;
    }

    public static String upCase(String str) {
        String tempstr = new String("");
        char tempch = ' ';
        for (int i = 0; i < str.length(); i++) {
            tempch = str.charAt(i);
            if (97 < str.charAt(i) && str.charAt(i) < 122)// 是小写字母
                tempch -= 32;
            tempstr += tempch;
        }
        return tempstr;
    }

    /**
     * 判断是否是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        int length = str.length();
        int v = 0;
        for (int i = 0; i < length; i++) {
            v = str.charAt(i);
            if (v < 49 || v > 57)
                return false;
        }

        return true;
    }

    /**
     * 分离str中的数字和字符。
     * 
     * @param str
     * @return
     * @author 玄承勇
     * @date 2014-5-20 下午4:58:37
     */
    public static String[] separateStringAndNumber(String str) {
        int length = str.length();
        char v = 0;
        StringBuffer string = new StringBuffer();
        StringBuffer number = new StringBuffer();
        for (int i = 0; i < length; i++) {
            v = str.charAt(i);
            if (v < 49 || v > 57)
                string.append(v);
            else
                number.append(v);
        }
        return new String[] { string.toString(), number.toString() };
    }

    /**
     * 取固定长度的字符串
     * 
     * @param resource
     *            原串
     * @param length
     *            长度
     * @return
     */
    public static String fixedLengthStringNull(String resource, int length) {
        if (resource.length() < length)
            return resource;

        return resource.subSequence(0, length) + "";
    }

    /**
     * 去固定长度的字符串，后面加...
     * 
     * @param resource
     * @param length
     * @return
     * @author 玄承勇
     * @date 2014-5-20 下午5:12:17
     */
    public static String fixedLengthString(String resource, int length) {
        return fixedLengthStringNull(resource, length) + "...";
    }

    /**
     * 把字符串中resource中的字符数组replace的所有敏感词替换为" "
     * 
     * @param resource
     * @param replace
     * @return
     * @author 玄承勇
     * @date 2014-5-20 下午5:11:04
     */
    public static String replaceToNull(String resource, String[] replace) {
        if (null == replace)
            return resource;
        String temp = resource;
        for (int i = 0; i < replace.length; i++) {
            temp = temp.replaceAll(replace[i], " ");
        }
        return temp;
    }

    /**
     * 按字节截取字符串
     */
    public static String substring(String content, int size, boolean suf) {
        if (content == null) {
            return "";
        }
        StringBuffer newStrSb = new StringBuffer();
        int totalCount = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (isSingleByte(c)) {
                totalCount++;
            } else {
                totalCount += 2;
            }
            if (totalCount <= size) {
                newStrSb.append(String.valueOf(c));
            } else {
                if (suf) {
                    newStrSb.append("...");
                }
                break;
            }
        }
        return newStrSb.toString();
    }

    public static int getByteLen(String content) {
        int totalCount = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (isSingleByte(c)) {
                totalCount++;
            } else {
                totalCount += 2;
            }
        }
        return totalCount;
    }

    /**
     * 是否为单字节字符
     * 
     * @Title isSingleByte
     * @author 吕凯
     * @date 2013-8-1 下午2:28:00
     * @param c
     * @return boolean
     */
    public static boolean isSingleByte(char c) {
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            return true;
        }
        return false;
    }

    public static String trancutString(String source, int number) {
        return substring(source, number - 1, true);
    }

    /**
     * /** 兼容原来 按汉字数计
     * 
     * @Title substring
     * @param source
     * @param number
     *            长度以汉字长度为准 1个英文或数字算半个字符
     * @return String
     */
    public static String substring(String source, int number) {
        return substring(source, number * 2 - 1, true);
    }

    /**
     * HTML字符转义，保存数据库用
     */
    public static String escapeHtml(String str) {
        if (str == null) {
            return null;
        } else {
            return str.trim().replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;");
        }
    }

    /**
     * 常用字符串分隔方法
     */
    public static String[] getStrArr(String source) {
        return source.split("，| | |,|、|;|；");
    }

    public static String formatLength(Object number, int length) {
        return String.format("%0" + length + "d", ClassUtil.obj2int(number));
    }

    /**
     * 格式化字节数 按字节数的大小格式化为Kb、Mb等实际大小
     * 
     * @Title byteFormat
     * @author 玄承勇
     * @date 2013-8-19 上午11:41:19
     * @param size
     *            原字节数
     * @param dec
     *            保留位数
     * @return String
     */
    public static String byteFormat(long size, int dec) {
        String[] sizearr = { " B", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB" };
        int pos = 0;
        double sized = (double) size;
        while (sized >= 1024) {
            sized /= 1024;
            pos++;
        }
        return ArithUtil.round(sized, dec) + "" + sizearr[pos];

    }

    /**
     * 格式化字节数 按字节数的大小格式化为Kb、Mb等实际大小
     * 
     * @Title byteFormat
     * @author 玄承勇
     * @date 2013-8-19 上午11:41:19
     * @param size
     * @return String
     */
    public static String byteFormat(long size) {
        return byteFormat(size, 2);

    }

    /**
     * 获取字符串的长度。汉字算2个长度
     * 
     * @param value
     * @return
     * @author 玄承勇
     * @date 2014-5-26 上午11:11:37
     */
    public static int length(String value) {
        value = value.trim();
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 按字节长度截取字符串(支持截取带HTML代 码样式的字符串)
     * 
     * @author 玄承勇
     * 
     * @version 1.0
     * 
     * @param param
     *            将要截取的字符串参数
     * @param length
     *            截取的字节长度
     * @param end
     *            字符串末尾补上的字符串
     * @return 返回截取后的字符串
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String subStringHTML(String param, int length, String end) {
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }

            if (!isCode && !isHTML) {
                n = n + 1;
                // UNICODE码字符占两个字节
                if ((temp + "").getBytes().length > 1) {
                    n = n + 1;
                }
            }

            result.append(temp);
            if (n >= length) {
                result.append(end);
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结束标记的HTML标记
        temp_result = temp_result
                .replaceAll(
                        "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                        "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);

        List endHTML = new ArrayList();

        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }

        return result.toString();
    }

    /**
     * 按html标签截字，html标签不处理原样返回
     * 
     * @Title subStringHTML
     * @author 吕凯
     * @date 2014-6-20 上午10:56:50
     * @param param
     *            需要截取的字符串
     * @param length
     *            汉字长度，英文按半个字算
     * @return String
     */
    public static String subStringHTML(String param, int chlength) {
        return subStringHTML(param, chlength * 2, "...");
    }

    /**
     * 移除字符串中的html标签
     * 
     * @Title removeHTMLTag
     * @author 吕凯
     * @date 2014-6-20 上午11:16:54
     * @param htmlStr
     * @return String
     */
    public static String removeHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "</?[A-z]+.*?>"; // 定义HTML标签的正则表达式
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 去掉字符串中的空格(不仅左右，也包括中间)
     * 
     * @param str
     * @return
     * @author 玄承勇
     * @date 2014-7-2 上午8:19:07
     */
    public static String getStringNoBlank(String str) {
        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return str;
        }
    }

    public static String substr(String s, int x) {
        if (s == null || x < 1)
            return "";
        else if (s.length() <= x) {
            return s;
        } else {
            return s.substring(0, x);
        }
    }

    /**
     * 把String 转化为long类型。
     * 
     * @param a
     * @return
     * @author 玄承勇
     * @date 2014-7-2 上午8:25:38
     */
    public static Long parstLong(String a) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        if (m.replaceAll("").trim() == "") {
            return 0l;
        }
        return Long.parseLong(m.replaceAll("").trim());
    }

    /**
     * 获取字符串中的数字
     * 
     * @param a
     * @return
     * @author 玄承勇
     * @date 2014-7-2 上午8:26:15
     */
    public static String getOnNum(String a) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        if (m.replaceAll("").trim().equals("")) {
            return "";
        }
        return m.replaceAll("").trim();
    }

    public static double parstDouble(String a) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        if (m.replaceAll("").trim().equals("")) {
            return 0;
        }
        try {
            return Double.parseDouble(m.replaceAll("").trim());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
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

    public static void main(String[] args) {
        String str = "ababccdd";
        System.out.println(strToAscii(str));
    }

}
