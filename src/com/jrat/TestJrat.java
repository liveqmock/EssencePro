package com.jrat;

import org.apache.log4j.Logger;

import com.util.LoggerUtil;

public class TestJrat {
    private static final Logger log = LoggerUtil.getLogger();

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    private static void test1() {
        for (int i = 0; i < 100; i++) {
            System.out.println("第" + i + "次");
        }

    }

}
