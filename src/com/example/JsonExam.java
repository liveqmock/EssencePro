package com.example;

import com.alibaba.fastjson.JSON;
import com.util.JsonUtil;

public class JsonExam {

    private String name;

    private String value;

    JsonExam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 
     * @Description: TODO
     * @param @param args
     * @return void
     * @throws
     * @author 玄承勇
     * @date 2014-4-18下午3:44:18
     */
    public static void main(String[] args) {
        JsonExam ex = new JsonExam("姓名", "黄泉颤抖");
        // fastjson
        System.out.println(JSON.toJSONString(ex));
        try {
            // JsonUtil——会抛出异常。建议使用fastjson
            System.out.println(JsonUtil.toJson(ex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
