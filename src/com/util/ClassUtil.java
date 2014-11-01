package com.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.annotation.Annotation.ColumnName;
import com.annotation.Annotation.NoFilterHtmlTag;

public class ClassUtil<T> {

    private static final Logger log = Logger.getLogger(ClassUtil.class);
    private static final String ROOT_PATH = WebUtil.getProjectAbsolutePath();

    /**
     * 查找路径packagename下。父类为superClass的类。
     * 
     * @param packagename
     * @param superClass
     * @return
     * @author 玄承勇
     * @date 2014-5-23 下午2:38:31
     */
    @SuppressWarnings("unchecked")
    public final static <T> List<Class<? extends T>> getClassList(String packagename, Class<T> superClass) {
        List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>();
        File directory = new File(ROOT_PATH + packagename.replace('.', '/'));
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (fileName.endsWith(".class")) {
                try {
                    Class<?> cl = Class.forName(packagename + '.' + fileName.replace(".class", ""));
                    if (superClass.isAssignableFrom(cl)) {
                        classes.add((Class<T>) cl);
                    }
                } catch (ClassNotFoundException e) {
                    LoggerUtil.getLogger().error("", e);
                }
            } else if (files[i].isDirectory()) {
                classes.addAll(getClassList(packagename + "." + fileName, superClass));
            }
        }
        return classes;
    }

    public static int obj2int(Object obj) {
        if (obj != null) {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    public static long obj2long(Object obj) {
        if (obj != null) {
            try {
                return Long.parseLong(obj.toString());
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    public static double obj2doulbe(Object obj) {
        if (obj != null) {
            try {
                return Double.parseDouble(obj.toString());
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    /**
     * obj转换为T,T为泛型类
     * 
     * @param obj
     * @param type
     * @return
     * @author 玄承勇
     * @datetime 2014-5-9 上午10:59:31
     */
    @SuppressWarnings("unchecked")
    public static <T> T obj2T(Object obj, Class<T> type) {
        if (obj == null) {
            return null;
        }
        Object model = null;
        if (type.equals(Integer.class) || type.equals(int.class)) {
            model = (obj.equals("")) ? null : new Integer(obj.toString());
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            model = (obj.equals("")) ? null : new Long(obj.toString());
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            model = (obj.equals("")) ? null : new Float(obj.toString());
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            model = (obj.equals("")) ? null : new Double(obj.toString());
        } else {
            model = obj;
        }
        return (T) model;
    }

    /**
     * 根据id获取model,泛型
     * 
     * @param modelBean
     * @return
     * @author 玄承勇
     * @date 2014-5-9 上午11:08:39
     */
    public static <T> Object getModelId(T modelBean) {
        return getModelValue(modelBean, "id");
    }

    /**
     * 根据name获取类中属性的Field属性的值，递归父类。
     * 
     * @param clazz
     * @param name
     * @return
     * @author 玄承勇
     * @date 2014-5-9 下午2:06:03
     */
    public static Field getField(Class<?> clazz, String name) {
        if (clazz == null || clazz.equals(Object.class)) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return getField(clazz.getSuperclass(), name);
    }

    /**
     * 把map转换为指定类型的model,model的属性忽略大小写
     * 
     * @param map
     * @param modelType
     *            转换的类型
     * @return
     * @author 玄承勇
     * @date 2014-5-16 下午4:29:23
     */
    public static <T> T map2model(Map<String, Object> map, Class<T> modelType) {
        try {
            Map<String, Object> newMap = new HashMap<String, Object>();
            // 过滤map中无效的key-value
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (key != null && value != null) {
                    newMap.put(key.toLowerCase(), value);
                }
            }

            T modelBean = modelType.newInstance();
            Map<String, Field> fieldMap = getFieldMap(modelType);
            for (Field field : fieldMap.values()) {
                // 判断是否为private属性,private 2
                if (field.getModifiers() == 2) {
                    String name = field.getName();
                    ColumnName columnName = field.getAnnotation(ColumnName.class);
                    if (columnName != null) {
                        name = columnName.value();
                    }
                    field.setAccessible(true);
                    Object value = newMap.get(name.toLowerCase());
                    try {
                        value = obj2T(value, field.getType());
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
     * 根据某类modelType的属性，组装成一个map,包括modelType的父类继承。
     * 
     * @param modelType
     * @return
     * @author 玄承勇
     * @date 2014-5-16 下午4:40:09
     */
    public static Map<String, Field> getFieldMap(Class<?> modelType) {
        Map<String, Field> map = new HashMap<String, Field>();
        // 获取此类型的所有继承类
        List<Class<?>> modelTypeList = getTypeList(modelType);
        for (Class<?> type : modelTypeList) {
            map.putAll(getThisFieldMap(type));
        }
        return map;
    }

    /**
     * 获取某类type的所有继承类
     * 
     * @param type
     * @return
     * @author 玄承勇
     * @date 2014-5-16 下午4:53:46
     */
    public static List<Class<?>> getTypeList(Class<?> type) {
        List<Class<?>> classList = new LinkedList<Class<?>>();
        classList.add(type);
        Class<?> tempType = type;
        while (!(tempType = tempType.getSuperclass()).equals(Object.class)) {
            classList.add(0, tempType);
        }
        return classList;
    }

    /**
     * 根据modelType获取此modelType的属性，组装成map。属性名全部小写
     * 
     * @param modelType
     * @return
     * @author 玄承勇
     * @date 2014-5-16 下午4:43:42
     */
    public static Map<String, Field> getThisFieldMap(Class<?> modelType) {
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

    /**
     * 获取obj的属性的值
     * 
     * @param model
     * @param fieldName
     * @return
     * @author 玄承勇
     * @date 2014-5-9 上午11:10:14
     */
    public static Object getModelValue(Object model, String fieldName) {
        try {
            Field field = getField(model.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(model);
            }
        } catch (Exception e) {
            log.error(model + "中找不到" + fieldName + "属性", e);
            log.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 设置obj中属性为fieldName中的值为value。
     * 
     * @param model
     * @param fieldName
     * @param value
     * @return
     * @author 玄承勇
     * @date 2014-5-9 下午2:26:38
     */
    public static boolean setModelValue(Object model, String fieldName, Object value) {
        try {
            Field field = ClassUtil.getField(model.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                if (value != null && !field.getType().equals(value.getClass())) {
                    value = obj2T(value, field.getType());
                }
                field.set(model, value);
                return true;
            }
        } catch (Exception e) {
            log.error("设置" + model + "中属性" + fieldName + "出错", e);
        }
        return false;
    }

    /**
     * 获取obj对象fieldName的Field
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                log.info(e);
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    // 对集合list进行排序。
    @SuppressWarnings("unchecked")
    public void Sort(List<T> list, final String method, final String sort) {

        Collections.sort(list, new Comparator() {

            public int compare(Object a, Object b) {

                int ret = 0;

                try {

                    Method m1 = ((T) a).getClass().getMethod(method, null);

                    Method m2 = ((T) b).getClass().getMethod(method, null);

                    if (sort != null && "desc".equals(sort))//

                        ret = m2.invoke(((T) b), null).toString().compareTo(m1.invoke(((T) a), null).toString());
                    else
                        //

                        ret = m1.invoke(((T) a), null).toString().compareTo(m2.invoke(((T) b), null).toString());
                } catch (NoSuchMethodException ne) {
                    log.error(ne);
                } catch (IllegalAccessException ie) {
                    log.error(ie);
                } catch (InvocationTargetException it) {
                    log.error(it);
                }
                return ret;
            }
        });
    }

    /**
     * 把字符串value按照指定的类型t转换
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午11:55:16
     * @param:
     * @param:
     * @return ： Object
     */
    public static Object convert(String value, Class<?> t) {
        if (value == null) {
            if (t.equals(java.lang.Boolean.class) || t.equals(Boolean.TYPE)) {
                value = "false";
                return new Boolean(value);

            } else {
                return null;
            }
        }

        if (t.equals(java.lang.Boolean.class) || t.equals(Boolean.TYPE)) {

            if (value.equals("1") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true"))
                value = "true";
            else
                value = "false";

            return new Boolean(value);
        }
        if (t.equals(java.lang.Byte.class) || t.equals(Byte.TYPE)) {
            return new Byte(value);
        }
        if (t.equals(java.lang.Character.class) || t.equals(Character.TYPE)) {
            return value.length() <= 0 ? null : new Character(value.charAt(0));
        }
        if (t.equals(java.lang.Short.class) || t.equals(Short.TYPE)) {
            return new Short(value);
        }
        if (t.equals(java.lang.Integer.class) || t.equals(Integer.TYPE)) {
            return new Integer(value);
        }
        if (t.equals(java.lang.Float.class) || t.equals(Float.TYPE)) {
            return new Float(value);
        }
        if (t.equals(java.lang.Long.class) || t.equals(Long.TYPE)) {
            return new Long(value);
        }
        if (t.equals(java.lang.Double.class) || t.equals(Double.TYPE)) {
            return new Double(value);
        }
        if (t.equals(java.lang.String.class)) {
            return value;
        }
        if (t.equals(java.io.File.class)) {
            return new File(value);
        }

        return null;
    }

    /**
     * 类似setModelValue方法
     * 
     * @param pd
     * @param parameterName
     * @param value
     * @param o
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @author 玄承勇
     * @date 2014-5-20 下午5:31:35
     */
    public static void invoke(PropertyDescriptor pd[], String parameterName, String value, Object o) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        for (int j = 0; j < pd.length; j++) {
            if (parameterName.equalsIgnoreCase(pd[j].getName())) {
                pd[j].getWriteMethod().invoke(o, ClassUtil.convert(value, pd[j].getPropertyType()));
                break;
            }
        }
    }

    /**
     * 把请求中的参数转换为指定的类型modelType。注：name与model中的属性名要对应。
     * 
     * @param request
     * @param modelType
     * @return
     * @author 玄承勇
     * @date 2014-5-24 上午11:33:13
     */
    public static <T> T getModel(HttpServletRequest request, Class<T> modelType) {
        try {
            Map<String, Field> fieldMap = getFieldMap(modelType);
            T t = modelType.newInstance();
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = request.getParameter(name);
                Field field = fieldMap.get(name.toLowerCase());
                if (field != null && value != null) {
                    field.setAccessible(true);
                    try {
                        if (field.getType().equals(String.class)) {// 过滤html标签
                            NoFilterHtmlTag noFilterHtmlTag = field.getAnnotation(NoFilterHtmlTag.class);
                            if (noFilterHtmlTag == null || !noFilterHtmlTag.value()) {
                                value = StringUtil.escapeHtml(value);
                            }
                        }
                        field.set(t, ClassUtil.obj2T(value, field.getType()));
                    } catch (Exception e) {
                        log.warn("form转换出错：fieldName:“" + name + "”，fieldValue:“" + value + "”");
                    }
                }
            }
            return t;
        } catch (Exception e) {
            log.error("创建model发生错误,uri:" + request.getRequestURI(), e);
            return null;
        }
    }
}
