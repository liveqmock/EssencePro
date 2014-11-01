package com.test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TesthtmlUnit {
    public static void main(String[] args) throws Exception {
        WebClient webClient = new WebClient();
        URL url = new URL("http://www.iteye.com");
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        System.out.println(page.getTitleText());

    }

}
