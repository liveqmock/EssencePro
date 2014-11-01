package com.service;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.sun.crypto.provider.SunJCE;
import com.util.LoggerUtil;

/**
 * 2013-2-23 上午11:35:37 @author 玄承勇 重构：解决性能问题、多线程出错问题
 * 
 */
public class SecurityService {
    private final static Logger log = LoggerUtil.getLogger();
    private final static Map<String, Cipher> cipherMap = new HashMap<String, Cipher>();
    private final static char[] DIGEST_ARRAY = "0123456789abcdef".toCharArray();

    public final static String SHA1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] b = md.digest(data.getBytes());
        return byte2hex(b);
    }

    public final static String MD5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(data.getBytes());
        return byte2hex(b);
    }

    private final static Cipher getEncryptCipher(String strKey) throws Exception {
        String key = strKey + "E";
        synchronized (cipherMap) {
            if (cipherMap.containsKey(key)) {
                return cipherMap.get(key);
            }
            Security.addProvider(new SunJCE());
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(strKey));
            cipherMap.put(key, cipher);
            return cipher;
        }
    }

    private final static Cipher getDecryptCipher(String strKey) throws Exception {
        String key = strKey + "D";
        synchronized (cipherMap) {
            if (cipherMap.containsKey(key)) {
                return cipherMap.get(key);
            }
            Security.addProvider(new SunJCE());
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(strKey));
            cipherMap.put(key, cipher);
            return cipher;
        }
    }

    // 用户提交信息加密方式
    public static String encryptIn(String strIn) throws Exception {
        return encrypt(strIn, "defaultKeyIn");
    }

    public static String decryptIn(String strIn) throws Exception {
        return decrypt(strIn, "defaultKeyIn");
    }

    // 项目间信息加密方式
    public static String encryptOut(String strIn) throws Exception {
        return encrypt(strIn, "defaultKeyOut");
    }

    public static String decryptOut(String strIn) throws Exception {
        return decrypt(strIn, "defaultKeyOut");
    }

    public static String encrypt(String strIn, String strKey) throws Exception {
        Cipher cipher = getEncryptCipher(strKey);
        byte[] strByte = cipher.doFinal(strIn.getBytes());
        return byte2hex(strByte);
    }

    public static String decrypt(String strIn, String strKey) throws Exception {
        Cipher cipher = getDecryptCipher(strKey);
        byte[] strByte = cipher.doFinal(hex2byte(strIn.getBytes()));
        return new String(strByte);
    }

    private final static Key getKey(String strKey) throws Exception {
        byte[] arrB = new byte[8];
        byte[] arrBTmp = strKey.getBytes();

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, "DES");
    }

    private final static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    private final static String byte2hex(byte[] b) {
        char[] c = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            byte b1 = b[i];
            c[(2 * i)] = DIGEST_ARRAY[((b1 & 0xF0) >> 4)];
            c[(2 * i + 1)] = DIGEST_ARRAY[(b1 & 0xF)];
        }
        return new String(c);
    }

    public static void main(String[] args) throws Exception {
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            final int num = i;
            new Thread() {
                public void run() {
                    try {
                        String s = SecurityService.encrypt("山东", "abcdefgh123" + num);
                        System.out.println(s);
                        s = SecurityService.decrypt(s, "abcdefgh123" + num);
                        System.out.println(s);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }.start();
        }
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) / 1000.000);

    }
}
