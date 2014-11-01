package com.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.util.LoggerUtil;

/**
 * 配置文件加载类
 * 
 */
public class PropertyModel {
    private final Logger log = LoggerUtil.getLogger();

    private ReentrantLock lock = new ReentrantLock();

    private Properties props = new Properties();

    private long lastmodifytime;
    private String filePath;
    private boolean isChange = false;

    public PropertyModel(String filePath) {
        this(filePath, 5 * 1000);// 默认每5秒钟自动同步一次
    }

    public PropertyModel(String filePath, long refreshTimeMillis) {
        this.filePath = filePath;
        reload();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadAndStore();
            }
        }, refreshTimeMillis, refreshTimeMillis);
    }

    public String getFilePath() {
        return filePath;
    }

    private void reload() {
        lock.lock();
        try {
            File file = new File(filePath);
            long modifttime = file.lastModified();
            if (modifttime > lastmodifytime) {// 如果文件已修改，以文件属性为准
                Properties tempProps = new Properties();
                tempProps.load(new FileInputStream(file));

                // 把properties文件合并进对象，冲突以对象为准
                for (Entry<Object, Object> e : tempProps.entrySet()) {
                    String key = e.getKey() + "";
                    String value = e.getValue() + "";
                    props.setProperty(key, value);
                }
                lastmodifytime = modifttime;
                log.info("properties加载文件，path:" + filePath);
            }
        } catch (IOException e) {
            log.error("加载属性文件失败！filePath:" + filePath, e);
        } finally {
            lock.unlock();
        }
    }

    private void loadAndStore() {
        reload();// 加载一次再保存
        if (isChange) {
            lock.lock();
            try {
                OutputStream fos = new FileOutputStream(filePath);
                try {
                    props.store(fos, "");
                    log.info("properties保存到文件，path:" + filePath);
                } finally {
                    lastmodifytime = System.currentTimeMillis();
                    fos.close();
                }
                isChange = false;
            } catch (IOException e) {
                log.error("属性文件保存出错！filePath:" + filePath, e);
            } finally {
                lock.unlock();
            }
        }
    }

    public Properties getProperties(String prefix) {
        Properties properties = new Properties();
        for (Entry<Object, Object> e : props.entrySet()) {
            String key = e.getKey() + "";
            if (key.startsWith(prefix)) {
                properties.put(key.substring(prefix.length()), e.getValue());
            }
        }
        return properties;
    }

    public String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
     * 返回属性值
     */
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getPropertyIntValue(String key, int defaultValue) {
        String intValueStr = getProperty(key, defaultValue + "");
        try {
            return Integer.parseInt(intValueStr);
        } catch (NumberFormatException e) {
            log.error("配置文件属性获取出错，filePath:" + filePath + "，key:“" + key + "”，value:“" + intValueStr + "”");
            return defaultValue;
        }
    }

    public long getPropertyLongValue(String key, long defaultValue) {
        String valueStr = getProperty(key, defaultValue + "");
        try {
            return Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            log.error("配置文件属性获取出错，filePath:" + filePath + "，key:“" + key + "”，value:“" + valueStr + "”");
            return defaultValue;
        }
    }

    public double getPropertyDoubleValue(String key, double defaultValue) {
        String valueStr = getProperty(key, defaultValue + "");
        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            log.error("配置文件属性获取出错，filePath:" + filePath + "，key:“" + key + "”，value:“" + valueStr + "”");
            return defaultValue;
        }
    }

    /**
     * 写入properties信息
     */
    public void writeProperty(String parameterName, String parameterValue) {
        props.setProperty(parameterName, parameterValue);
        isChange = true;
    }

    /**
     * 写入properties信息
     */
    public void writeProperties(Map<String, Object> proMap) {
        if (proMap == null || proMap.isEmpty()) {
            return;
        }
        for (String key : proMap.keySet()) {
            props.setProperty(key, proMap.get(key) + "");
        }
        isChange = true;
    }
}
