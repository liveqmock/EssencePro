package com.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    /**
     * 
     * @description : 去除掉bigdecimal类型小数点后不需要的0
     * @author : 玄承勇
     * @datetime: 2014-5-1 下午11:11:15
     * @param:
     * @param: String
     */
    public static String filterEndWithZero(BigDecimal dec) {
        BigDecimal dec45 = dec.setScale(4, BigDecimal.ROUND_HALF_UP);
        String dec_str = String.valueOf(dec45);

        return ArithUtil.subZeroAndDot(dec_str);
    }

}
