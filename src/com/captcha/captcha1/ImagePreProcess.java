package com.captcha.captcha1;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

/**
 * 字体固定。大小固定。位置固定验证码。
 * 
 * @author Administrator
 * 
 */
public class ImagePreProcess {
    // 验证码识别基本分为四步。图片预处理，分割，训练，识别

    // 1、图像的预处理——根据亮度设置阀值处理。黑白化。去除干扰线。
    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
            return 1;
        }
        return 0;
    }

    public static int isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
            return 1;
        }
        return 0;
    }

    /**
     * 移除干扰线
     * 
     * @param picFile
     * @return
     * @throws Exception
     */
    public static BufferedImage removeBackgroud(String picFile) throws Exception {
        BufferedImage img = ImageIO.read(new File(picFile));
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (isWhite(img.getRGB(x, y)) == 1) {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    img.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return img;
    }

    // 2、分割
    public static List<BufferedImage> splitImage(BufferedImage img) throws Exception {
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
        subImgs.add(img.getSubimage(10, 6, 8, 10));
        subImgs.add(img.getSubimage(19, 6, 8, 10));
        subImgs.add(img.getSubimage(28, 6, 8, 10));
        subImgs.add(img.getSubimage(37, 6, 8, 10));
        return subImgs;
    }

    // 训练。导入训练库。
    public static Map<BufferedImage, String> loadTrainData() throws Exception {
        Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
        File dir = new File("C:\\train");
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
    public static String getAllOcr(String file) throws Exception {
        BufferedImage img = removeBackgroud(file);
        List<BufferedImage> listImg = splitImage(img);
        Map<BufferedImage, String> map = loadTrainData();
        String result = "";
        for (BufferedImage bi : listImg) {
            result += getSingleCharOcr(bi, map);
        }
        ImageIO.write(img, "JPG", new File("C:\\" + result + ".jpg"));
        return result;
    }

    // 下载验证码图片
    public static void downloadImage() {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://www.puke888.com/authimg.php");
        for (int i = 0; i < 30; i++) {
            try {
                // 执行getMethod
                int statusCode = httpClient.executeMethod(getMethod);
                if (statusCode != HttpStatus.SC_OK) {
                    System.err.println("Method failed: " + getMethod.getStatusLine());
                }
                // 读取内容
                String picName = "C://pic//" + i + ".jpg";
                InputStream inputStream = getMethod.getResponseBodyAsStream();
                OutputStream outStream = new FileOutputStream(picName);
                IOUtils.copy(inputStream, outStream);
                outStream.close();
                System.out.println("OK!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放连接
                getMethod.releaseConnection();
            }
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // downloadImage();
        for (int i = 0; i < 30; ++i) {
            String text = getAllOcr("C://pic//" + i + ".jpg");
            System.out.println(i + ".jpg = " + text);
        }
    }
}
