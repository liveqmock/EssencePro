package com.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtil {

    public static Logger log = getLogger();

    public static Logger getLogger() {
        PropertyConfigurator.configure(WebUtil.getProjectAbsolutePath() + "log4j.properties");
        return Logger.getLogger(Thread.currentThread().getClass());
    }

}
