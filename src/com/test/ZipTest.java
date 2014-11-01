package com.test;

import org.junit.Test;

import com.compress.ZipUtil;

public class ZipTest {

    @Test
    public void test1() throws Exception {
        // 压缩文件
        ZipUtil.compress("d:\\f.txt");
        // 压缩目录
        ZipUtil.compress("d:\\fd");
    }

    @Test
    public void test2() throws Exception {
        // 解压到指定目录
        ZipUtil.decompress("d:\\f.txt.zip", "d:\\ff");
        // 解压到当前目录
        ZipUtil.decompress("d:\\fd.zip");
    }
}
