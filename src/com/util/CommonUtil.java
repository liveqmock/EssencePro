package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

public class CommonUtil {

    /* 人民币大写转换 */
    public static String convertToChineseNumber(double number) {
        StringBuffer chineseNumber = new StringBuffer();
        boolean pand = false;
        String[] num = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[] unit = { "分", "角", "圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万" };
        String tempNumber = String.valueOf(Math.round((number * 100)));

        if (tempNumber != null && tempNumber.length() > 1 && tempNumber.charAt(0) == '-') {
            pand = true;
            tempNumber = tempNumber.substring(1, tempNumber.length());

        }
        int tempNumberLength = tempNumber.length();
        if ("0".equals(tempNumber)) {
            return "零圆整";
        }
        if (tempNumberLength > 15) {
            // throw new InputException("超出转化范围.");
            System.out.println(("超出转化范围"));
        }
        boolean preReadZero = true; // 前面的字符是否读零
        for (int i = tempNumberLength; i > 0; i--) {
            if ((tempNumberLength - i + 2) % 4 == 0) {
                // 如果在（圆，万，亿，万）位上的四个数都为零,如果标志为未读零，则只读零，如果标志为已读零，则略过这四位
                if (i - 4 >= 0 && "0000".equals(tempNumber.substring(i - 4, i))) {
                    if (!preReadZero) {
                        chineseNumber.insert(0, "零");
                        preReadZero = true;
                    }
                    i -= 3; // 下面还有一个i--
                    continue;
                }
                // 如果当前位在（圆，万，亿，万）位上，则设置标志为已读零（即重置读零标志）
                preReadZero = true;
            }
            Integer digit = Integer.parseInt(tempNumber.substring(i - 1, i));
            if (digit == 0) {
                // 如果当前位是零并且标志为未读零，则读零，并设置标志为已读零
                if (!preReadZero) {
                    chineseNumber.insert(0, "零");
                    preReadZero = true;
                }
                // 如果当前位是零并且在（圆，万，亿，万）位上，则读出（圆，万，亿，万）
                if ((tempNumberLength - i + 2) % 4 == 0) {
                    chineseNumber.insert(0, unit[tempNumberLength - i]);
                }
            }
            // 如果当前位不为零，则读出此位，并且设置标志为未读零
            else {
                chineseNumber.insert(0, num[digit] + unit[tempNumberLength - i]);
                preReadZero = false;
            }
        }
        // 如果分角两位上的值都为零，则添加一个“整”字
        if (tempNumberLength - 2 >= 0 && "00".equals(tempNumber.substring(tempNumberLength - 2, tempNumberLength))) {
            chineseNumber.append("整");
        }
        if (pand)
            chineseNumber.insert(0, "负");
        return chineseNumber.toString();
    }

    public static String ObjectOutput(Object Object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(Object);
            String out = new String(baos.toByteArray());

            return out;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object ObjectInput(String str) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
            ObjectInputStream in = new ObjectInputStream(input);
            return in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把money的字符串格式为123,456,79类型
     * 
     * @param money
     * @return
     * @author 玄承勇
     * @date 2014-5-19 下午4:05:40
     */
    public static String formatMoney(double d) {
        DecimalFormat df = new java.text.DecimalFormat(",###.00"); // 四舍六入五奇偶
        String rtValue = df.format(d);
        if (rtValue.indexOf(".") == 0)
            return "0" + rtValue;
        if (rtValue.indexOf("-.") == 0)
            return "-0" + rtValue.substring(1);

        return rtValue;
    }

}
