package com.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.model.AreaProbModel;

public class AreaUtil {

    private static final Map<String, String> allProvinceMap = new HashMap<String, String>();
    private static final Map<String, String> allCityMap = new HashMap<String, String>();
    private static final Map<String, String> allQuMap = new HashMap<String, String>();
    private static final Map<String, String> allAreaMap = new HashMap<String, String>();
    private static Logger log = LoggerUtil.getLogger();
    static {
        try {
            loadAreaData();
        } catch (IOException e) {
            log.error("加载地域文件出错！", e);
        }
    }

    private static void loadAreaData() throws IOException {
        String file = WebUtil.getProjectAbsolutePath() + "area.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String temp = null;
        while ((temp = br.readLine()) != null) {
            putAreaMap(temp);
        }
    }

    private static void putAreaMap(String areaInfoStr) {
        String[] areaFields = areaInfoStr.split(",");
        if (areaFields.length == 2) {
            String number = areaFields[0];
            String name = areaFields[1];
            if (number.length() == 6) {
                allAreaMap.put(number, name);
                if (number.endsWith("0000")) {
                    allProvinceMap.put(number, name);
                } else if (number.endsWith("00")) {
                    allCityMap.put(number, name);
                } else {
                    allQuMap.put(number, name);
                }
            }
        }
    }

    // 通过省名称获得id
    public static String getProvinceIdByName(String name) {
        for (Entry<String, String> entry : allProvinceMap.entrySet()) {
            if (entry.getValue().substring(0, 2).equalsIgnoreCase(name.substring(0, 2))) {
                return entry.getKey();
            }
        }
        return "";
    }

    // 通过省id获得，省下辖市
    public static Map<String, String> getCitiesByProvinceById(String id) {
        Map<String, String> hash = new HashMap<String, String>();
        for (Entry<String, String> entry : allCityMap.entrySet()) {
            if (id.substring(0, 2).equalsIgnoreCase(entry.getKey().substring(0, 2)) && id.substring(2, 4) != "00") {
                hash.put(entry.getKey(), entry.getValue());
            }
        }
        return hash;
    }

    // 通过省id获得，省下辖市
    public static Map<String, String> getCitiesByProvinceName(String name) {
        return getCitiesByProvinceById(getProvinceIdByName(name));
    }

    /**
     * 通过市id获取市下面所有的区
     * 
     * @param id
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午11:35:02
     */
    public static Map<String, String> getQuByCityId(String id) {
        Map<String, String> hash = new HashMap<String, String>();
        for (Entry<String, String> entry : allQuMap.entrySet()) {
            if (id.substring(0, 4).equalsIgnoreCase(entry.getKey().substring(0, 4)) && id.substring(4, 6) != "00") {
                hash.put(entry.getKey(), entry.getValue());
            }
        }
        return hash;
    }

    /**
     * 通过省id获取省下面所有的区
     * 
     * @param id
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午11:35:34
     */
    public static Map<String, String> getQuByProvinceId(String id) {
        Map<String, String> hash = new HashMap<String, String>();
        for (Entry<String, String> entry : allQuMap.entrySet()) {
            if (id.substring(0, 2).equalsIgnoreCase(entry.getKey().substring(0, 2)) && id.substring(4, 6) != "00") {
                hash.put(entry.getKey(), entry.getValue());
            }
        }
        return hash;
    }

    /**
     * 通过区id获取区所在的市
     * 
     * @param id
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午11:36:03
     */
    public Entry<String, String> getCityByQuid(String id) {
        return new AbstractMap.SimpleEntry<String, String>(id, allCityMap.get(id.substring(0, 4) + "00"));
    }

    /**
     * 通过区id获取区所在的省
     * 
     * @param id
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午11:36:30
     */
    public Entry<String, String> getProvinceByQuid(String id) {
        return new AbstractMap.SimpleEntry<String, String>(id, allProvinceMap.get(id.substring(0, 2) + "0000"));
    }

    /**
     * 根据区获取区的全名：xx省xx市xx区
     * 
     * @param id
     * @param name
     * @return
     * @author 玄承勇
     * @date 2014-5-21 上午11:37:39
     */
    public Entry<String, String> getFullByQu(String id, String name) {
        return new AbstractMap.SimpleEntry<String, String>(id, allProvinceMap.get(id.substring(0, 2) + "0000")
                + allCityMap.get(id.substring(0, 4) + "00") + name);
    }

    /**
     * 添加地域
     * 
     * @param entry
     * @param intProb
     * @param list
     * @author 玄承勇
     * @date 2014-5-21 上午11:39:12
     */
    private static void addArea(Entry<String, String> entry, int intProb, List<AreaProbModel> list) {
        AreaProbModel prob = new AreaProbModel();
        prob.setId(entry.getKey());
        prob.setName(entry.getValue());
        prob.setProb(intProb);
        list.add(prob);
    }

    private static void addAreaAndFlag(String info, Map<String, String> areaMap, int topflag, int firstFlag, int secondFlag, int firstProb,
            int secondProb, List<AreaProbModel> list, Integer flag) {
        if (topflag != 0) {
            flag = topflag;
        }
        for (Entry<String, String> entry3 : areaMap.entrySet()) {
            if (info.contains(entry3.getValue())) {// 概率高31
                flag = firstFlag;
                addArea(entry3, firstProb, list);
            } else if (info.contains(dealSubTwo(entry3.getValue()))) {// 概率低32
                flag = secondFlag;
                addArea(entry3, secondProb, list);
            }
        }
    }

    private static void addAreaNoFlag(String info, Map<String, String> areaMap, int firstProb, int secondProb, List<AreaProbModel> list) {
        for (Entry<String, String> entry3 : areaMap.entrySet()) {
            if (info.contains(entry3.getValue())) {// 概率高31
                addArea(entry3, firstProb, list);
            } else if (info.contains(dealSubTwo(entry3.getValue()))) {// 概率低32
                addArea(entry3, secondProb, list);
            }
        }
    }

    public static Entry<String, String> getAreaInfo(String info) {
        // 针对分公司的处理
        if (info == null) {
            return null;
        }
        if (info.indexOf("分公司") > 5) {
            // 提取前面的五个字
            int position = info.indexOf("分公司");
            if (position > 6 && (!info.substring(position - 6, position).contains("公司"))) {
                return getAreaInfo(info.substring(position - 6, position));
            }
            return getSubAreaInfo(info.substring(position - 5, position));
        }
        // 1 确定所在省
        List<AreaProbModel> list = new ArrayList<AreaProbModel>();
        Integer flag = 0;
        for (Entry<String, String> entry : allProvinceMap.entrySet()) {
            if (info.contains(entry.getValue())) { // 概率相对高11
                // 根据省id获取下辖市
                // 比较下辖市
                flag = 1;// 准确找到了省了
                for (Entry<String, String> entry2 : getCitiesByProvinceById(entry.getKey()).entrySet()) {
                    if (info.contains(entry2.getValue())) {// 概率高
                        // flag = 2;// 准确找到了市了，省市，都是精准定位的，但是没有找到区
                        // 取出所有下辖区
                        addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 2, 11, 12, 99, 89, list, flag);
                    } else if (info.contains(dealSubTwo(entry2.getValue()))) {// 概率低
                        // flag = 3;// 模糊找到市了，省是精准定位的，但是没有找到区
                        addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 3, 13, 14, 92, 78, list, flag);
                    }
                }
            } else if (info.contains(dealSubTwo(entry.getValue()))) {// //
                flag = 9;// 模糊找到了省了
                for (Entry<String, String> entry2 : getCitiesByProvinceById(entry.getKey()).entrySet()) {
                    if (info.contains(entry2.getValue())) {// 概率高21
                        // 取出所有下辖区
                        // flag = 6;
                        addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 6, 15, 16, 83, 73, list, flag);
                    } else if (info.contains(dealSubTwo(entry2.getValue()))) {// 概率低22
                        // flag = 7;
                        addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 7, 17, 18, 76, 69, list, flag);
                    }
                }
            }
        }
        if (flag < 10 && flag != 0) {
            if (flag == 2 || flag == 3 | flag == 6 || flag == 7) {
                // 找到了，省市，但是没有找到区，这个也算是完成任务了，重新查找所在的省市
                for (Entry<String, String> entry : allProvinceMap.entrySet()) {
                    if (info.contains(entry.getValue())) { // 概率相对高11
                        // 根据省id获取下辖市
                        // 比较下辖市
                        addAreaNoFlag(info, getCitiesByProvinceById(entry.getKey()), 72, 67, list);
                    } else if (info.contains(dealSubTwo(entry.getValue()))) {
                        addAreaNoFlag(info, getCitiesByProvinceById(entry.getKey()), 70, 62, list);
                    }
                }
            } else if (flag == 1 || flag == 9) {
                // 只是找到了所在省，没有找到所在市,首先查找所在省，然后，遍历省下有的区
                for (Entry<String, String> entry : allProvinceMap.entrySet()) {
                    if (info.contains(entry.getValue())) { // 概率相对高11
                        // 根据省获取该省下辖所有的县
                        addAreaAndFlag(info, getQuByProvinceId(entry.getKey()), 0, 21, 22, 59, 49, list, flag);
                    } else if (info.contains(dealSubTwo(entry.getValue()))) {
                        addAreaAndFlag(info, getQuByProvinceId(entry.getKey()), 0, 23, 24, 58, 48, list, flag);
                    }
                }
            }
        }
        // 如果只找到省信息，没有找到下辖信息
        if (flag == 1 || flag == 9) {
            flag = 100;
            for (Entry<String, String> entry : allProvinceMap.entrySet()) {
                if (info.contains(entry.getValue())) {
                    addArea(entry, 21, list);
                } else if (info.contains(dealSubTwo(entry.getValue()))) {
                    addArea(entry, 11, list);
                }
            }
        }
        // 没有找到省的处理,活着只是找到省的信息的处理
        if (flag == 0 || flag == 100) {
            // 只是找到市的处理
            // 如果没有返回值表示没有找到对应的匹配
            for (Entry<String, String> entry2 : allCityMap.entrySet()) {
                if (info.contains(entry2.getValue()) && entry2.getValue().length() > 1) {// 概率高21
                    // 取出所有下辖区
                    // flag = 91;
                    addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 91, 81, 82, 85, 75, list, flag);

                } else if (info.contains(dealSubTwo(entry2.getValue())) && entry2.getValue().length() > 1) {// 概率低22
                    // flag = 92;
                    addAreaAndFlag(info, getQuByCityId(entry2.getKey()), 92, 83, 84, 56, 46, list, flag);
                }
            }
            // 如果只是找到了市没有找到下辖区的处理
            if (flag == 91 || flag == 92) {
                // 把所属的市信息打印出来
                for (Entry<String, String> entry : allCityMap.entrySet()) {
                    if (info.contains(entry.getValue()) && entry.getValue().length() > 1) {
                        addArea(entry, 10, list);
                    } else if (info.contains(dealSubTwo(entry.getValue())) && entry.getValue().length() > 1) {
                        addArea(entry, 6, list);
                        flag = 100;
                    }
                }
            }
            if (flag == 0 || flag == 100) {
                for (Entry<String, String> entry : allQuMap.entrySet()) {
                    if (info.contains(entry.getValue())) {// 概率高31
                        if (entry.getValue().length() > 2) {
                            addArea(entry, 7, list);
                        } else {
                            addArea(entry, 6, list);
                        }
                    } else if (info.contains(dealSubTwo(entry.getValue()))) {// 概率低32
                        addArea(entry, 5, list);
                    }
                }
            }
        }
        // 遍历list
        return getMostProbableArea(list, info);
    }

    // 分公司情况的处理
    private static Entry<String, String> getSubAreaInfo(String info) {
        List<AreaProbModel> list = new ArrayList<AreaProbModel>();
        // 省
        addProbableArea2List(list, allProvinceMap, 60, 50, info);
        // 获得市信息
        addProbableArea2List(list, allCityMap, 90, 80, info);
        // 获得区信息
        addProbableArea2List(list, allQuMap, 70, 60, info);

        return getMostProbableArea(list, "");
    }

    private static void addProbableArea2List(List<AreaProbModel> list, Map<String, String> areaMap, int firstProb, int secondProb,
            String info) {
        for (Entry<String, String> entry : areaMap.entrySet()) {
            if (entry.getValue().length() > 1) {
                if (info.contains(entry.getValue())) {
                    AreaProbModel prob = new AreaProbModel();
                    prob.setId(entry.getKey());
                    prob.setName(entry.getValue());
                    prob.setProb(firstProb);
                    list.add(prob);
                } else if (info.contains(dealSubTwo(entry.getValue()))) {
                    AreaProbModel prob = new AreaProbModel();
                    prob.setId(entry.getKey());
                    prob.setName(entry.getValue());
                    prob.setProb(secondProb);
                    list.add(prob);
                }
            }
        }
    }

    // 获取列表中最有可能的地域信息
    private static Entry<String, String> getMostProbableArea(List<AreaProbModel> list, String info) {
        AreaProbModel big = null;
        for (AreaProbModel ar : list) {
            if (big == null) {
                big = ar;
            } else {
                if (big.getProb() < ar.getProb()) {
                    big = ar;
                } else if (big.getProb() == ar.getProb()) {
                    // 比较Index 值,小值优先考虑
                    if (info.indexOf(big.getName()) > info.indexOf(ar.getProb())) {
                        big = ar;
                    }
                }
            }
        }
        if (big == null) {
            return null;
        } else {
            return getAreaInfo(big.getId(), big.getName());
        }
    }

    public static String dealSubTwo(String name) {
        if (name.length() > 2) {
            return name.substring(0, 2);
        }
        return name;
    }

    // 根据地址id获取地址全程
    public static Entry<String, String> getAreaInfo(String id, String name) {
        if ("0000".equalsIgnoreCase(id.substring(2, 6))) {
            // 省级
            return new AbstractMap.SimpleEntry<String, String>(id, name);
        } else if ("00".equalsIgnoreCase(id.substring(4, 6))) {
            // 根据id获得省市名称
            return new AbstractMap.SimpleEntry<String, String>(id, allProvinceMap.get(id.substring(0, 2) + "0000") + name);
        } else
            // 根据id获得省市区名称
            return new AbstractMap.SimpleEntry<String, String>(id, allProvinceMap.get(id.substring(0, 2) + "0000")
                    + allCityMap.get(id.substring(0, 4) + "00") + name);

    }

}
