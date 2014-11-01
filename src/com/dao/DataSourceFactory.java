package com.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.model.ConfigModel;
import com.util.LoggerUtil;

public class DataSourceFactory {
    private static final Logger log = LoggerUtil.getLogger();

    private static Map<String, DataSource> DATA_SOURCE_MAP = new HashMap<String, DataSource>();
    private static Map<String, ThreadLocal<Connection>> CONNECTION_THREAD_LOCAL_MAP = new HashMap<String, ThreadLocal<Connection>>();

    // 获取数据源
    public static DataSource getDataSource(String key) {
        DataSource dataSource = DATA_SOURCE_MAP.get(key);
        if (dataSource == null) {
            synchronized (DATA_SOURCE_MAP) {
                dataSource = DATA_SOURCE_MAP.get(key);
                if (dataSource == null) {
                    dataSource = createDataSource(key);
                    DATA_SOURCE_MAP.put(key, dataSource);
                }
            }
        }
        return dataSource;
    }

    // 获取连接线程
    public static ThreadLocal<Connection> getThreadLocalConnection(String key) {
        ThreadLocal<Connection> connectionThreadLocal = CONNECTION_THREAD_LOCAL_MAP.get(key);
        if (connectionThreadLocal == null) {
            synchronized (CONNECTION_THREAD_LOCAL_MAP) {
                connectionThreadLocal = CONNECTION_THREAD_LOCAL_MAP.get(key);
                if (connectionThreadLocal == null) {
                    connectionThreadLocal = new ThreadLocal<Connection>();
                    CONNECTION_THREAD_LOCAL_MAP.put(key, connectionThreadLocal);
                }
            }
        }
        return connectionThreadLocal;
    }

    public static void main(String[] args) {
        System.out.println(ConfigModel.CONFIG.getProperty("db0_connectionProperties"));
    }

    public static DataSource createDataSource(String prefix) {
        Properties properties = ConfigModel.CONFIG.getProperties(prefix);
        log.warn("加载数据库，url:" + properties.getProperty("url"));
        try {
            return DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error("数据库加载失败", e);
            System.exit(-1);
            return null;
        }
    }

}
