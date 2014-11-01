package com.model;

import com.util.WebUtil;

public class ConfigModel {

    public static final PropertyModel CONFIG = new PropertyModel(WebUtil.getProjectAbsolutePath() + "config.properties", 10 * 1000);
    public static final PropertyModel MARK = new PropertyModel(WebUtil.getProjectAbsolutePath() + "mark.properties", 10 * 1000);

}
