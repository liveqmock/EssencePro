package com.messycode;

import java.io.UnsupportedEncodingException;

public class MessyCode {
    public MessyCode() {

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 1、解决表单提交中文乱码
        String formMsg = "abcdefg";
        String str = new String(formMsg.getBytes("ISO-8859-1"), "utf-8");
    }

}
