package com.HttpUnit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.util.LoggerUtil;

public class TestHttpUnit {
    private static final Logger log = LoggerUtil.getLogger();

    public static void main(String[] args) {
        TestHttpUnit service = new TestHttpUnit();
        try {
            service.testUserHttpUnit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUserHttpUnit() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

        /** HtmlUnit请求web页面 */
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); // 禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page = wc.getPage("http://www.1688.com/?spm=a260j.615.5095973.78");

        DomNodeList<DomElement> links = page.getElementsByTagName("a");

        for (DomElement link : links) {
            System.out.println(link.asText() + "  " + link.getAttribute("href"));
        }
    }
}
