package com.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.captcha.captcha1.CoordinateModel;
import com.util.ImgUtil;

public class TestCaptcha {
    // 验证码识别基本分为四步。图片预处理，分割，训练，识别

    // 1、图像的预处理——根据亮度设置阀值处理。黑白化。去除干扰线。
    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getBlue() + color.getGreen() > 200) {
            return 1;
        }
        return 0;
    }

    public static BufferedImage removeBackgroud(String picFile) throws Exception {
        BufferedImage img = ImageIO.read(new File(picFile));
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isWhite(img.getRGB(x, y)) == 1) {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    img.setRGB(x, y, Color.black.getRGB());
                }
            }
        }
        return img;
    }

    // 2、分割
    public static List<BufferedImage> splitImage(BufferedImage img) {
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
        subImgs.add(img.getSubimage(0, 0, 40, 40));// 第一个阿拉伯数字
        subImgs.add(img.getSubimage(40, 0, 30, 40));// 运算符
        subImgs.add(img.getSubimage(70, 0, 22, 40));// 第二个阿拉伯数字
        return subImgs;
    }

    // 3、倾斜矫正
    // 采用旋转卡壳算法。主要原理是针对X轴投影法。验证码的字符倾斜角度一般在(-45度,45度)之间
    // 水平的字符，X轴投影最短时，即为应该旋转的角度。此时字符矫正完毕
    // 初始点坐标：x0,y0。对应角度为b。旋转angle角度后，新坐标为x1,y1。对应角度为a。假设长度为r
    // 推论：x0=r*cos(b);y0=r*sin(b);
    // x1=r*cos(b-a) =r*cos(b)*cos(a)+r*sin(b)*sin(a)=x0*cos(a)+y0*sin(a)
    // y1=r*sin(b-a) =r*sin(b)*cos(a)-r*cos(b)*sin(a)=-x0*sin(a)+y0*cos(a);

    public static CoordinateModel revolveAngle(float x0, float y0, int degrees) {
        double radians = ConvertDegreesToRadians(degrees);// 角度转化为弧度
        double x1 = x0 * Math.cos(radians) + y0 * Math.sin(radians);
        double y1 = -x0 * Math.sin(radians) + y0 * Math.cos(radians);
        CoordinateModel model = new CoordinateModel(x1, y1);
        return model;
    }

    public static double ConvertDegreesToRadians(int degrees) {
        double radians = (Math.PI / 180) * degrees;
        return radians;
    }

    public static void rotateImage(String file) throws IOException {
        BufferedImage img = ImgUtil.rotateImage(new File(file), -10);
        File rotatefile = new File("C:\\pic\\rotateFile.jpg");
        ImageIO.write(img, "JPEG", rotatefile);

    }

    public static void getMiniDegrees(String picFile) throws IOException {

        BufferedImage img = ImageIO.read(new File(picFile));
        int width = img.getWidth();
        int height = img.getHeight();
        int x_len = getXLength(img);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isWhite(img.getRGB(x, y)) == 0) {// 是黑点像素

                }
            }
        }
    }

    public static int getXLength(BufferedImage img) {
        int minxX = Integer.MAX_VALUE;
        int maxX = Integer.MAX_VALUE;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isWhite(img.getRGB(x, y)) == 0) {// 是黑点像素
                    if (x < minxX) {
                        minxX = x;
                    } else if (x > maxX) {
                        maxX = x;
                    }
                }
            }
        }
        return maxX - minxX;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 18; i++) {
            String file = "C:\\pic\\" + i + ".jpg";
            BufferedImage bi = removeBackgroud(file);
            // List<BufferedImage> list = splitImage(bi);
            // for (int i = 0; i < list.size(); i++) {
            // File newFile = new File("C:\\pic\\new" + (i + 1) + ".jpg");
            // ImageIO.write(list.get(i), "jpg", newFile);
            // }
            // rotateImage(file);
            ImageIO.write(bi, "JPEG", new File("C:\\pic\\black" + i + ".jpeg"));
            BufferedImage image = ImageIO.read(new File(file));
            System.out.println("==================");
        }

    }

}
