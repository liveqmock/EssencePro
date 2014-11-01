package com.util.ip;

import java.util.Arrays;
import java.util.List;

public class AddressUtil {

    private String[] countrys = new String[] { "", "" };
    /**
     * 省
     */
    private static String[] provinces = new String[] { "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南",
            "广东", "广西", "海南", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "台湾" };
    /**
     * 直辖市
     */
    private static String[] municipalities = new String[] { "北京", "天津", "上海", "重庆", "香港", "澳门" };
    /**
     * 自治区
     */
    private static String[] autoregion = new String[] { "内蒙古", "广西", "新疆", "宁夏", "西藏" };

    public static List<String> PROVINCES_LIST = Arrays.asList(provinces);
    public static List<String> MUNICIPALITIES_LIST = Arrays.asList(municipalities);
    public static List<String> AUTOREGION_LIST = Arrays.asList(autoregion);
}
