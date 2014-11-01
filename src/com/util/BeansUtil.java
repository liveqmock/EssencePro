package com.util;

import com.model.StaticModel;

public class BeansUtil {

    // 获取定义的所有bean的名字
    public static String[] getAllBeansName() {
        return StaticModel.WEB_APP_CONTEXT.getBeanDefinitionNames();
    }

}
