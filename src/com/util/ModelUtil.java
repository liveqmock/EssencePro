package com.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.annotation.Annotation.ColumnName;

public class ModelUtil {
    private final static Logger log = LoggerUtil.getLogger();

    private static Map<String, Object> model2map(Object modelBean, Class<?> modelType, boolean hasNullField) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = modelType.getDeclaredFields();
        for (Field field : fields) {
            // 判断是否为private属性
            if (field.getModifiers() == 2) {
                String name = field.getName();
                ColumnName columnName = field.getAnnotation(ColumnName.class);
                if (columnName != null) {// 注释名优先
                    name = columnName.value();
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(modelBean);
                    if (hasNullField || value != null) {
                        map.put(name, value);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        return map;
    }

    private static Map<String, Field> getThisFieldMap(Class<?> modelType) {
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Field[] fields = modelType.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            ColumnName columnName = field.getAnnotation(ColumnName.class);
            if (columnName != null) {// 注释名优先
                name = columnName.value();
            }
            fieldMap.put(name.toLowerCase(), field);
        }

        return fieldMap;
    }

    // 获取所有父类列表，直到Object类（不含Object类）
    private static List<Class<?>> getTypeList(Class<?> type) {
        List<Class<?>> classList = new LinkedList<Class<?>>();
        classList.add(type);
        Class<?> tempType = type;
        while (!(tempType = tempType.getSuperclass()).equals(Object.class)) {
            classList.add(0, tempType);
        }
        return classList;
    }

    public static Map<String, Object> model2map(Object modelBean, boolean hasNullField) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Class<?>> modelTypeList = getTypeList(modelBean.getClass());
        for (Class<?> modelType : modelTypeList) {
            map.putAll(model2map(modelBean, modelType, hasNullField));
        }

        return map;
    }

    public static Map<String, Object> model2map(Object modelBean) {
        return model2map(modelBean, false);
    }

    public static Map<String, Field> getFieldMap(Class<?> modelType) {
        Map<String, Field> map = new HashMap<String, Field>();
        List<Class<?>> modelTypeList = getTypeList(modelType);
        for (Class<?> type : modelTypeList) {
            map.putAll(getThisFieldMap(type));
        }
        return map;
    }

    public static void copyModel(Object fromModel, Object toModel) {
        Map<String, Field> fromModelMap = getFieldMap(fromModel.getClass());
        Map<String, Field> toModelMap = getFieldMap(toModel.getClass());
        for (Entry<String, Field> e : toModelMap.entrySet()) {
            Field fromField = fromModelMap.get(e.getKey());
            // 只有复制私有变量
            if (fromField != null && fromField.getModifiers() == 2) {
                try {
                    fromField.setAccessible(true);
                    Object value = fromField.get(fromModel);
                    Field field = e.getValue();
                    if (value != null && value.getClass().equals(field.getType())) {
                        field.setAccessible(true);
                        field.set(toModel, value);

                    }
                } catch (Exception e1) {
                    log.error("", e1);
                }
            }
        }
    }

    // 忽略属性大小写
    public static <T> T map2model(Map<String, Object> map, Class<T> modelType) {
        try {
            Map<String, Object> newMap = new HashMap<String, Object>();
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (key != null && value != null) {
                    newMap.put(key.toLowerCase(), value);
                }
            }

            T modelBean = modelType.newInstance();
            Map<String, Field> fieldMap = getFieldMap(modelType);
            for (Field field : fieldMap.values()) {
                // 判断是否为private属性
                if (field.getModifiers() == 2) {
                    String name = field.getName();
                    ColumnName columnName = field.getAnnotation(ColumnName.class);
                    if (columnName != null) {
                        name = columnName.value();
                    }
                    field.setAccessible(true);
                    Object value = newMap.get(name.toLowerCase());
                    try {
                        value = ClassUtil.obj2T(value, field.getType());
                        field.set(modelBean, value);
                    } catch (Exception e) {
                        log.error("字段设置错误，字段名：“" + name + "”，字段值：“" + value + "”，" + e.getMessage());
                    }
                }
            }
            return modelBean;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 转换所有属性为null且为String类型==》""
     */
    public static void confirmValues(Object modelBean) {
        Map<String, Field> fieldMap = getFieldMap(modelBean.getClass());
        for (Field field : fieldMap.values()) {
            // 判断是否为private属性
            if (field.getModifiers() == 2) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(modelBean);
                    if (value == null) {
                        if (field.getType().equals(String.class)) {
                            field.set(modelBean, "");
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    private static Field getField(Class<?> clazz, String name) {
        if (clazz == null || clazz.equals(Object.class)) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            ColumnName columnName = field.getAnnotation(ColumnName.class);
            if (columnName != null) {
                fieldName = columnName.value();
            }
            if (fieldName.equals(name)) {
                return field;
            }
        }
        return getField(clazz.getSuperclass(), name);
    }

    public static Object getModelValue(Object model, String fieldName) {
        try {
            Field field = getField(model.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(model);
            }
            log.error("getModelValue属性未找到", new Exception());
        } catch (Exception e) {
            log.error(model + "中找不到" + fieldName + "属性", e);
            log.warn(e.getMessage());
        }
        return null;
    }

    public static boolean setModelValue(Object model, String fieldName, Object value) {
        try {
            Field field = getField(model.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                if (value != null && !field.getType().equals(value.getClass())) {
                    value = ClassUtil.obj2T(value, field.getType());
                }
                field.set(model, value);
                return true;
            }
            log.error("setModelValue属性未找到", new Exception());
        } catch (Exception e) {
            log.error("设置" + model + "中属性" + fieldName + "出错", e);
        }
        return false;
    }

    public static <T> Object getModelId(T modelBean) {
        return getModelValue(modelBean, "id");
    }
}
