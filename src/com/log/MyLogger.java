package com.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class MyLogger {
    MyLogger() {
        // 读取Logger文件的配置分为properties和xml
        // 读取properties
        PropertyConfigurator.configure("log.properties");
        // 读取xml
        DOMConfigurator.configure("src/log.xml");
    }

    // 获取日志记录器的方法(Logger)
    // 1、root日志记录器,是所有logger的祖先，总是存在的，不可通过名字获取。
    Logger logRoot = Logger.getRootLogger();
    // 2、创建新的日志记录器
    Logger logMy = Logger.getLogger("MyLogger");
    // 3、比较常用的方法——根据类名实例化一个静态的全局日志记录器,在某对象中，用该对象所属的类作为参数，是最理智的命名方法。
    static Logger log = Logger.getLogger(MyLogger.class);

}
