package com.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.captcha.captcha1.CoordinateModel;
import com.util.ImgUtil;

public class TestCaptcha {
    private static Map<BufferedImage, String> map_first = new HashMap<BufferedImage, String>();
    private static Map<BufferedImage, String> map_operation = new HashMap<BufferedImage, String>();
    private static Map<BufferedImage, String> map_second = new HashMap<BufferedImage, String>();
    private static List<Integer> list = new ArrayList<Integer>();

    static {
        list.add(44);
        list.add(54);
        list.add(64);
        list.add(74);
        list.add(94);
    }

    // 吉林验证码识别思路：1、去除干扰线。2、按照【等】字进行左右切割。3、获取左侧运算式图片后，划分为五类:44,54,64,74,94。
    // 4、每一类按照汉阿符进行切割。5、建立训练库。三类。阿汉大。6、返回匹配度最高的图片首字符。

    private static int getMinModulus(int width) {
        int minModulus = 100;
        int width_format = 0;
        for (int i = 0; i < list.size(); i++) {
            int formatNumber = list.get(i);
            int modulus = Math.abs(formatNumber - width);
            if (modulus < minModulus) {
                minModulus = modulus;
                width_format = formatNumber;
            }
        }
        return width_format;
    }

    // 验证码识别基本分为四步。图片预处理，分割，训练，识别

    // 1、图像的预处理——根据亮度设置阀值处理。黑白化。去除干扰线。
    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getBlue() + color.getGreen() > 350) {
            return 1;
        }
        return 0;
    }

    // 去除干扰线
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

    public static BufferedImage splitImageForXYWD(BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    // 2、分割
    public static List<BufferedImage> splitImage(BufferedImage img) {
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
        // 已经调试为最佳比例。勿动。
        // subImgs.add(img.getSubimage(8, 0, 30, 40));// 第一个阿拉伯数字
        // subImgs.add(img.getSubimage(40, 0, 28, 40));// 运算符
        // 已经调试为最佳比例。勿动。
        subImgs.add(img.getSubimage(68, 0, 22, 40));// 第二个阿拉伯数字
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

    // 训练。导入训练库。
    public static Map<BufferedImage, String> loadTrainData(Map<BufferedImage, String> map, String train) throws Exception {
        File dir = new File(train);
        File[] files = dir.listFiles();
        for (File file : files) {
            map.put(ImageIO.read(file), file.getName().charAt(0) + "");
        }
        return map;
    }

    // 获取每个字符图片最相似的值。
    public static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
        String result = "";
        int width = img.getWidth();
        int height = img.getHeight();
        int min = width * height;
        for (BufferedImage bi : map.keySet()) {
            int count = 0;
            Label1: for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (isWhite(img.getRGB(x, y)) != isWhite(bi.getRGB(x, y))) {
                        count++;
                        if (count >= min)
                            break Label1;
                    }
                }
            }
            if (count < min) {
                min = count;
                result = map.get(bi);
            }
        }
        return result;
    }

    // 获取验证码。
    public static void getAllOcr(String file, int i) throws Exception {
        BufferedImage img = removeBackgroud(file);
        // BufferedImage left_img = getLeftImage(img);
        // BufferedImage first_img = splitImageForXYWD(img, 74, 0, 21, 50);
        // BufferedImage operation_img = splitImageForXYWD(img, 40, 0, 28, 40);
        // BufferedImage second_img = splitImageForXYWD(img, 68, 0, 22, 40);
        //
        // String first_str = getSingleCharOcr(first_img, map_first);
        // String ope_str = getSingleCharOcr(operation_img, map_operation);
        // String second_str = getSingleCharOcr(second_img, map_second);
        // String result = first_str + getOperation(ope_str) + second_str;
        // splitLeftImage(left_img, i);

        ImageIO.write(img, "PNG", new File("C:\\test\\black_white" + i + ".png"));
    }

    // 按照【等】字进行左右切割
    private static BufferedImage getLeftImage(BufferedImage img) throws IOException {
        BufferedImage deng_img = ImageIO.read(new File("C:\\base\\deng.jpg"));
        int width_deng = deng_img.getWidth();
        int width = img.getWidth();
        int max = 0;
        int x_perfix = 0;
        for (int x = 0; x < width - width_deng; x++) {
            BufferedImage eachImage = img.getSubimage(x, 0, width_deng, 50);
            int count = isEqualNumber(eachImage, deng_img);
            if (count > max) {
                max = count;
                x_perfix = x;
            }
        }
        x_perfix = getMinModulus(x_perfix);
        BufferedImage left_Image = img.getSubimage(0, 0, x_perfix, 50);
        return left_Image;
    }

    // 五类处理
    private static void splitLeftImage(BufferedImage left_Image, int i) throws IOException {
        if (left_Image != null) {
            int width = left_Image.getWidth();
            switch (width) {
            case 44:
                split44(left_Image, i);
                break;
            case 54:
                split54(left_Image, i);
                break;
            case 64:
                split64(left_Image, i);
                break;
            case 74:
                split74(left_Image, i);
                break;
            case 94:
                split94(left_Image, i);
                break;
            default:
                System.out.println(("当前截取的长度异常。宽度为：" + width));
                break;
            }
        }
    }

    /**
     * 44——阿符阿
     * 
     * @param img
     * @throws IOException
     */
    private static void split44(BufferedImage img, int i) throws IOException {
        if (img.getWidth() == 44) {
            BufferedImage first_img = img.getSubimage(0, 0, 24, 50);
            BufferedImage ope_img = img.getSubimage(24, 0, 8, 50);
            BufferedImage second_img = img.getSubimage(32, 0, 12, 50);
            ImageIO.write(first_img, "JPG", new File("C:\\picsult\\44\\" + i + "_first.jpg"));
            ImageIO.write(ope_img, "JPG", new File("C:\\picsult\\44\\" + i + "_ope.jpg"));
            ImageIO.write(second_img, "JPG", new File("C:\\picsult\\44\\" + i + "_second.jpg"));
        }
    }

    /**
     * 94——汉汉2汉
     * 
     * @param img
     * @throws IOException
     */
    private static void split94(BufferedImage img, int i) throws IOException {
        if (img.getWidth() == 94) {
            BufferedImage first_img = img.getSubimage(0, 0, 32, 50);
            BufferedImage ope_img = img.getSubimage(32, 0, 40, 50);
            BufferedImage second_img = img.getSubimage(72, 0, 22, 50);
            ImageIO.write(first_img, "JPG", new File("C:\\picsult\\94\\" + i + "_first.jpg"));
            ImageIO.write(ope_img, "JPG", new File("C:\\picsult\\94\\" + i + "_ope.jpg"));
            ImageIO.write(second_img, "JPG", new File("C:\\picsult\\94\\" + i + "_second.jpg"));
        }
    }

    /**
     * 74——汉汉汉或符汉2符——目前无法区分哪种类型。后期处理。
     * 
     * @param img
     * @param i
     * @throws IOException
     */
    private static void split74(BufferedImage img, int i) throws IOException {
        if (img.getWidth() == 74) {
            // 判断是否是阿汉2阿
            boolean bool = isAH2A(img);
            if (bool) {

            } else {// 为汉汉汉。
                BufferedImage first_img = img.getSubimage(0, 0, 32, 50);
                BufferedImage ope_img = img.getSubimage(32, 0, 20, 50);
                BufferedImage second_img = img.getSubimage(52, 0, 22, 50);
                ImageIO.write(first_img, "JPG", new File("C:\\picsult\\74\\" + i + "_first.jpg"));
                ImageIO.write(ope_img, "JPG", new File("C:\\picsult\\74\\" + i + "_ope.jpg"));
                ImageIO.write(second_img, "JPG", new File("C:\\picsult\\74\\" + i + "_second.jpg"));
            }

        }
    }

    /**
     * 判断是否为阿汉2阿
     * 
     * @param img
     * @return
     * @throws IOException
     */
    private static boolean isAH2A(BufferedImage img) throws IOException {
        int width = img.getWidth();
        BufferedImage jianqu_img = ImageIO.read(new File("C:\\jilin_train\\base\\jianqu.jpg"));
        int width_jianqu = jianqu_img.getWidth();
        int max = 0;
        for (int x = 0; x < width - width_jianqu; x++) {
            BufferedImage eachImage = img.getSubimage(x, 0, width_jianqu, 50);
            int count = isEqualNumber(eachImage, jianqu_img);
            if (count > max) {
                max = count;
            }
        }

        System.out.println(max);

        return false;
    }

    /**
     * 64——汉符汉
     * 
     * @param img
     * @param i
     * @throws IOException
     */
    private static void split64(BufferedImage img, int i) throws IOException {
        if (img.getWidth() == 64) {
            BufferedImage first_img = img.getSubimage(0, 0, 34, 50);
            BufferedImage ope_img = img.getSubimage(34, 0, 8, 50);
            BufferedImage second_img = img.getSubimage(42, 0, 22, 50);
            ImageIO.write(first_img, "JPG", new File("C:\\picsult\\64\\" + i + "_first.jpg"));
            ImageIO.write(ope_img, "JPG", new File("C:\\picsult\\64\\" + i + "_ope.jpg"));
            ImageIO.write(second_img, "JPG", new File("C:\\picsult\\64\\" + i + "_second.jpg"));
        }
    }

    /**
     * 54——阿汉阿
     * 
     * @param img
     * @param i
     * @throws IOException
     */
    private static void split54(BufferedImage img, int i) throws IOException {
        if (img.getWidth() == 54) {
            BufferedImage first_img = img.getSubimage(0, 0, 22, 50);
            BufferedImage ope_img = img.getSubimage(22, 0, 21, 50);
            BufferedImage second_img = img.getSubimage(43, 0, 11, 50);
            ImageIO.write(first_img, "JPG", new File("C:\\picsult\\54\\" + i + "_first.jpg"));
            ImageIO.write(ope_img, "JPG", new File("C:\\picsult\\54\\" + i + "_ope.jpg"));
            ImageIO.write(second_img, "JPG", new File("C:\\picsult\\54\\" + i + "_second.jpg"));
        }
    }

    private static int isEqualNumber(BufferedImage image1, BufferedImage image2) {
        int width1 = image1.getWidth();
        int height1 = image1.getHeight();
        int width2 = image2.getWidth();
        int height2 = image2.getHeight();
        int count = 0;
        if ((width1 == width2) && (height1 == height2)) {
            for (int x = 0; x < width1; x++) {
                for (int y = 0; y < height1; y++) {
                    if (image1.getRGB(x, y) == image2.getRGB(x, y)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static String getOperation(String ope) {
        if (null != ope && ope.trim().length() > 0) {
            if (ope.equals("j")) {
                return "+";
            } else if (ope.equals("c")) {
                return "X";
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        // loadTrainData(map_first, "C:\\train_first");
        // loadTrainData(map_operation, "C:\\train_ope");
        // loadTrainData(map_second, "C:\\train_second");
        for (int i = 0; i < 100; i++) {
            getAllOcr("C:\\pic\\pic_" + i + ".png", i);
        }
        System.out.println("============");

    }
}
