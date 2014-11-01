package com.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generic<T> {
    private Class<T> entityClass;

    /**
     * 获取泛型的类型T
     * 
     * @return
     * @author 玄承勇
     * @date 2014-5-9 下午4:55:02
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getModelClass() {
        try {
            // 获取实际运行的类的Class
            Class<?> clazz = getClass();
            // 获取实际运行的类的直接超类的泛型类型
            Type type = clazz.getGenericSuperclass();
            // 如果该泛型类型是参数化类型
            if (type instanceof ParameterizedType) {
                // 获取泛型类型的实际类型参数集
                Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
                // 取出第一个(下标为0)参数的值
                entityClass = (Class<T>) parameterizedType[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityClass;
    }

    // 上面方法的简化
    @SuppressWarnings("unchecked")
    protected Class<T> getModelClassSimplify() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    // 泛型的实际类型参数的类全名
    public String getEntityName() {
        return entityClass.getName();
    }

    // 泛型的实际类型参数的类名
    public String getEntitySimpleName() {
        return entityClass.getSimpleName();
    }

    // 泛型的实际类型参数的Class
    public Class<T> getEntityClass() {
        return entityClass;
    }

}
