package com.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.compress.ZLibUtil;
import com.util.LoggerUtil;

public class TestZLib {

    private static final Logger log = LoggerUtil.getLogger();

    @Test
    public final void testBytes() {
        System.out.println("字节压缩／解压缩测试");
        String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zlex.org";
        System.out.println("输入字符串:\t" + inputStr);
        byte[] input = inputStr.getBytes();
        System.out.println("输入字节长度:\t" + input.length);

        byte[] data = ZLibUtil.compress(input);
        System.out.println("压缩后字节长度:\t" + data.length);
        byte[] output = ZLibUtil.decompress(data);
        System.out.println("解压缩后字节长度:\t" + output.length);
        String outputStr = new String(output);
        System.out.println("输出字符串:\t" + outputStr);

        assertEquals(inputStr, outputStr);
    }

    @Test
    public final void testFile() {
        String filename = "zlib";
        File file = new File(filename);
        System.out.println("文件压缩／解压缩测试");
        String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zlex.org";
        System.out.println("输入字符串:\t" + inputStr);
        byte[] input = inputStr.getBytes();
        System.out.println("输入字节长度:\t" + input.length);

        try {

            FileOutputStream fos = new FileOutputStream(file);
            ZLibUtil.compress(input, fos);
            fos.close();
            System.out.println("压缩后字节长度:\t" + file.length());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        byte[] output = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            output = ZLibUtil.decompress(fis);
            fis.close();

        } catch (Exception e) {
            fail(e.getMessage());
        }
        System.out.println("解压缩后字节长度:\t" + output.length);
        String outputStr = new String(output);
        System.out.println("输出字符串:\t" + outputStr);

        assertEquals(inputStr, outputStr);
    }
}
