package com.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.log4j.Logger;

import com.util.ModelUtil;

public class MyBeanProcessor extends BeanProcessor {

    private static final Logger log = Logger.getLogger(MyBeanProcessor.class);

    public <T> T toBean(ResultSet rs, Class<T> modelType) throws SQLException {
        try {
            T t = modelType.newInstance();
            Map<String, Field> fieldMap = ModelUtil.getFieldMap(modelType);
            return type2Bean(rs, t, fieldMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> modelType) throws SQLException {
        List<T> results = new ArrayList<T>();
        try {
            Map<String, Field> fieldMap = ModelUtil.getFieldMap(modelType);
            while (rs.next()) {
                try {
                    T t = modelType.newInstance();
                    results.add(type2Bean(rs, t, fieldMap));
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return results;
    }

    private <T> T type2Bean(ResultSet rs, T model, Map<String, Field> fieldMap) throws SQLException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount() + 1;
        for (int i = 1; i < cols; i++) {
            String colName = meta.getColumnLabel(i).toLowerCase();
            Field field = fieldMap.get(colName);
            if (field != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object value = rs.getObject(i);
                try {
                    if (value == null) {
                    } else if (value != null && fieldType.equals(Date.class) && value.getClass().equals(String.class)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            field.set(model, sdf.parse((String) value));
                        } catch (ParseException e) {
                            log.error("日期字段读取失败," + fieldType + ",error:" + e.getMessage());
                        }
                    } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                        field.set(model, rs.getInt(i));
                    } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                        field.set(model, rs.getLong(i));
                    } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                        field.set(model, rs.getFloat(i));
                    } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                        field.set(model, rs.getDouble(i));
                    } else {
                        field.set(model, value);
                    }
                } catch (Exception e) {
                    log.error("fieldType:" + fieldType + ",fieldName:" + colName + ",value:" + value + "," + e.getMessage());
                }
            } else {
                // log.info(model.getClass() + "中缺少对应属性---数据库列名:" + colName);
            }
        }
        // 暂不开启查询confirm，防止查询后更新操作出错
        // Utils.confirmValues(model);
        return model;
    }

    public static void main(String[] args) {
        System.out.println(int.class);
        System.out.println(Integer.class);
    }

}
