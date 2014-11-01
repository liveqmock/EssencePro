package com.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class AuthCodeModel {

    private static final String CHARS = "346789ABCEFGHJKMNPQRTUVWXYabcdefghjkmnpqrtuvwxy"; // 获取的验证码从其中获

    private BufferedImage image;// 图像
    private String str;// 验证

    public AuthCodeModel() {
        int width = 55, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0xf1f2f3));
        g.fillRect(0, 0, width, height);
        Random random = new Random();
        // 干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(getRandColor(0, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(50);
            int yl = random.nextInt(50);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 随机数
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String randChar = CHARS.charAt(random.nextInt((CHARS.length() - 1))) + "";
            sRand += randChar;
            g.setFont(new Font("Arial", Font.PLAIN, 19));
            Color color = getRandColor(0, 150);
            g.setColor(color);
            g.drawString(randChar, 3 + i * 12, 13 + random.nextInt(6));
        }
        g.dispose();

        this.str = sRand;
        this.image = image;
    }

    /*
     * 给定范围获得随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /*
     * 取得验证码图
     */
    public BufferedImage getImage() {
        return image;
    }

    /*
     * 取得图片的验证码
     */
    public String getString() {
        return str.toLowerCase();
    }
}
