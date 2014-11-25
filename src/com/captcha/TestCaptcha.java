package com.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.util.ImgUtil;

public class TestCaptcha {
    // 验证码识别基本分为四步。图片预处理，分割，训练，识别

    // 1、图像的预处理——根据亮度设置阀值处理。黑白化。去除干扰线。
    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getBlue() + color.getGreen() > 180) {
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

    public static void rotateImage(String file) throws IOException {
        BufferedImage img = ImgUtil.rotateImage(new File(file), -10);
        File rotatefile = new File("C:\\pic\\rotateFile.jpg");
        ImageIO.write(img, "JPEG", rotatefile);

    }

    public static void main(String[] args) throws Exception {
        String file = "C:\\pic\\new1.jpg";
        // BufferedImage bi = removeBackgroud(file);
        // List<BufferedImage> list = splitImage(bi);
        // for (int i = 0; i < list.size(); i++) {
        // File newFile = new File("C:\\pic\\new" + (i + 1) + ".jpg");
        // ImageIO.write(list.get(i), "jpg", newFile);
        // }
        // rotateImage(file);
        BufferedImage image = ImageIO.read(new File(file));
        int x = image.getMinX();
        int y = image.getMinY();
        System.out.println(x);
        System.out.println(y);

        System.out.println("==================");
    }

}
