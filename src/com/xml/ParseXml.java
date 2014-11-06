package com.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.util.LoggerUtil;
import com.util.PathUtil;

/**
 * 解析获取xml文件内容。
 * 
 * @author Administrator
 * 
 */
public class ParseXml {

    private static final Logger log = LoggerUtil.getLogger();

    public static void main(String[] args) {
        ParseXml service = new ParseXml();
        // 加载配置文件
        service.loadXmlFile("test");

    }

    private void loadXmlFile(String areaName) {
        try {
            File f = new File(PathUtil.getProjectAbsolutePath() + "/com/xml/" + areaName + ".xml");
            if (!f.exists()) {
                log.error("xml文件不存在。");
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            getTableName(doc);
            getRequest(doc);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void getRequest(Document doc) {
        getUrl(doc);
        getMethod(doc);
        boolean bool = Boolean.parseBoolean(getNeedHeader(doc));
        if (bool) {
            getHeaders(doc);
        }
        getParams(doc);
    }

    private void getParams(Document doc) {
        NodeList nl = doc.getElementsByTagName("param");
        for (int i = 0; i < nl.getLength(); i++) {
            Element ele = (Element) nl.item(i);
            getParam(doc, ele);
        }
    }

    private void getParam(Document doc, Element ele) {
        String key = getElementByTagName(ele, "name");
        System.out.println(key);
        String value = getElementByTagName(ele, "value");
        System.out.println(value);

    }

    private void getHeaders(Document doc) {
        NodeList nl = doc.getElementsByTagName("header");
        for (int i = 0; i < nl.getLength(); i++) {
            Element ele = (Element) nl.item(i);
            getHeader(doc, ele);
        }

    }

    private void getHeader(Document doc, Element ele) {
        String key = getElementByTagName(ele, "key");
        System.out.println(key);
        String value = getElementByTagName(ele, "value");
        System.out.println(value);

    }

    private String getElementByTagName(Element ele, String key) {
        NodeList nodelist = ele.getElementsByTagName(key);
        String value = nodelist.item(0).getFirstChild().getNodeValue();
        return value;
    }

    private String getNeedHeader(Document doc) {
        return getNodeValue(doc, "needHeader", "是否需要设置表头");
    }

    private void getMethod(Document doc) {
        getNodeValue(doc, "method", "method");

    }

    private void getUrl(Document doc) {
        getNodeValue(doc, "url", "url");

    }

    private String getTableName(Document doc) {
        return getNodeValue(doc, "TABLENAME", "表名");
    }

    private String getNodeValue(Document doc, String nodeName, String prefix) {
        NodeList nl = doc.getElementsByTagName(nodeName);
        if (null != nl && nl.getLength() == 1) {
            String value = nl.item(0).getFirstChild().getNodeValue();
            System.out.println(prefix + "为：" + value);
            return value;
        }

        return null;
    }
}
