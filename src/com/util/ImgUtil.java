package com.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.devlib.schmidt.imageinfo.ImageInfo;

public class ImgUtil {
    private final static Logger log = LoggerUtil.getLogger();

    private final static int FONT_SIZE = 12;

    public static String str2WhiteBgImg(String text, int maxByteSize, boolean hasIns) {
        BufferedImage bufferedImage = str2Img(text, maxByteSize, new Color(255, 255, 255), new Color(1, 1, 1), hasIns);
        return image2base64(bufferedImage);
    }

    public static String str2RedBgImg(String text, int maxByteSize, boolean hasIns) {
        BufferedImage bufferedImage = str2Img(text, maxByteSize, new Color(186, 6, 3), new Color(255, 255, 255), hasIns);
        return image2base64(bufferedImage);
    }

    public static String str2GrayBgImg(String text, int maxByteSize, boolean hasIns) {
        BufferedImage bufferedImage = str2Img(text, maxByteSize, new Color(187, 187, 187), new Color(0, 0, 0), hasIns);
        return image2base64(bufferedImage);
    }

    public static String str2RedFontImg(String text, int maxByteSize, boolean hasIns) {
        BufferedImage bufferedImage = str2Img(text, maxByteSize, new Color(255, 255, 255), new Color(186, 6, 3), hasIns);
        return image2base64(bufferedImage);
    }

    private static BufferedImage str2Img(String text, int maxByteSize, Color bgColor, Color textColor, boolean hasIns) {
        text = StringUtil.substring(text, maxByteSize, true);
        int width = StringUtil.getByteLen(text) * FONT_SIZE / 2;
        return str2Img(text, width, 16, bgColor, textColor, hasIns, 0, 12);
    }

    // 用文件流验证图片格式
    public static boolean isImage(final InputStream in) {
        ImageInfo ii = new ImageInfo();
        ii.setInput(in);
        return ii.check();
    }

    public static boolean setImageSize(File imageFile, int width, int height) {
        try {
            Image image = read(imageFile);
            if (image == null) {
                log.error("图片格式不正确，path:" + imageFile.getAbsolutePath());
                return false;
            }
            return saveImage2File(image, imageFile, width, height);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    private static BufferedImage read(File imageFile) throws IOException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            image = readJpeg(imageFile);
        }
        return image;
    }

    public static boolean setCmykImageSize(File imageFile, int width, int height) {
        BufferedImage image;
        try {
            image = readJpeg(imageFile);
            return saveImage2File(image, imageFile, width, height);
        } catch (IOException e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 写入一个 JPG 图像
     * 
     * @param im
     *            图像对象
     * @param targetJpg
     *            目标输出 JPG 图像文件
     * @param quality
     *            质量 0.1f ~ 1.0f
     */
    public static void writeJpeg(RenderedImage im, Object targetJpg, float quality) {
        try {
            ImageWriter writer = ImageIO.getImageWritersBySuffix("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            ImageOutputStream os = ImageIO.createImageOutputStream(targetJpg);
            writer.setOutput(os);
            writer.write((IIOMetadata) null, new IIOImage(im, null, null), param);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 尝试读取JPEG文件的高级方法,可读取32位的jpeg文件
     */
    private static BufferedImage readJpeg(File imageFile) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        while (readers.hasNext()) {
            reader = (ImageReader) readers.next();
            if (reader.canReadRaster()) {
                break;
            }
        }
        ImageInputStream input = ImageIO.createImageInputStream(imageFile);
        reader.setInput(input);
        // Read the image raster
        Raster raster = reader.readRaster(0, null);
        BufferedImage image = createJPEG4(raster);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeJpeg(image, out, 1);
        out.flush();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
        return img;
    }

    /**
     * Java的ImageIO无法处理4-component图像并且Java2D不能应用于AffineTransformOp,所以将栅格数据转换为RGB
     * . Technique due to MArk Stephens. Free for any use.
     */
    private static BufferedImage createJPEG4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];

        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);

        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i], cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
        }

        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[] { 0, 1, 2 }, null);

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }

    public static boolean setImageSize(File imageFile, int width) {
        try {
            BufferedImage image = read(imageFile);
            if (image == null) {
                log.error("图片格式不正确，path:" + imageFile.getAbsolutePath());
                return false;
            }
            int iw = image.getWidth();// 原始图象的宽度
            int ih = image.getHeight();// 原始图象的高度
            int height = ih;
            if (iw > width) {
                height = ih * width / iw;
            } else {
                width = iw;
            }

            return saveImage2File(image, imageFile, width, height);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    private static boolean saveImage2File(Image image, File saveImageFile, int width, int height) {
        if (image == null) {
            return false;
        }
        try {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = buffImg.getGraphics();
            g.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            ImageIO.write(buffImg, "JPEG", saveImageFile);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    public static boolean setWaterMark(File imageFile, File markFile, String markDir) {
        try {
            BufferedImage sourceBufferedImage = read(imageFile);
            if (sourceBufferedImage == null) {
                log.error("图片格式不正确，path:" + imageFile.getAbsolutePath());
                return false;
            }
            int width = 700;
            int height = 467;
            // 如果高度大于宽度，为竖版
            if (sourceBufferedImage.getHeight() > sourceBufferedImage.getWidth()) {
                width = 467;
                height = 700;
            }
            BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newBufferedImage.createGraphics();
            g.drawImage(sourceBufferedImage, 0, 0, width, height, null);
            // 水印文件
            BufferedImage markImage = read(markFile);
            int width_biao = markImage.getWidth();
            int height_biao = markImage.getHeight();
            g.rotate(-Math.PI / 4);// 倾斜45度
            for (int i = 0; i < 4; i++) {
                g.drawImage(markImage, i * 215 - 445, 360, width_biao, height_biao, null);
            }
            g.dispose();
            // 输出图像
            String fileName = imageFile.getName();
            File markImg = new File(markDir);
            if (!markImg.exists()) {
                markImg.mkdirs();
            }
            ImageIO.write(newBufferedImage, "JPEG", new File(markDir, fileName + "_mark"));
            saveImage2File(newBufferedImage, new File(markDir, fileName + "_mark_min"), 120, 80);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    private static String image2base64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 输出图片根据x，y坐标
     * 
     * @Title str2Img
     * @author 玄承勇
     * @date 2013-7-31 下午2:42:31
     */
    private final static BufferedImage str2Img(String text, int width, int height, Color bgColor, Color textColor, boolean hasIns,
            int left, int top) {
        width = (width <= 0) ? 1 : width;
        width = (hasIns) ? width + 84 : width;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("宋体", Font.PLAIN, FONT_SIZE));
        g.setColor(textColor);
        g.drawString(text, left, top);

        if (hasIns) {
            g.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 11));
            g.drawString("【11315.com】", width - 84, 12);
        }
        return image;
    }

    public static boolean rotateImg(File imageFile, int degree) {
        try {
            BufferedImage image = read(imageFile);
            int iw = image.getWidth();// 原始图象的宽度
            int ih = image.getHeight();// 原始图象的高度
            int w = 0;
            int h = 0;
            int x = 0;
            int y = 0;
            degree = degree % 360;
            if (degree < 0)
                degree = 360 + degree;// 将角度转换到0-360度之间
            double ang = Math.toRadians(degree);// 将角度转为弧度

            /**
             * 确定旋转后的图象的高度和宽度
             */

            if (degree == 180 || degree == 0 || degree == 360) {
                w = iw;
                h = ih;
            } else if (degree == 90 || degree == 270) {
                w = ih;
                h = iw;
            } else {
                double cosVal = Math.abs(Math.cos(ang));
                double sinVal = Math.abs(Math.sin(ang));
                w = (int) (sinVal * ih) + (int) (cosVal * iw);
                h = (int) (sinVal * iw) + (int) (cosVal * ih);
            }

            x = (w / 2) - (iw / 2);// 确定原点坐标
            y = (h / 2) - (ih / 2);
            BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
            Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();

            AffineTransform at = new AffineTransform();
            at.rotate(ang, w / 2, h / 2);// 旋转图象
            at.translate(x, y);
            gs.setTransform(at);
            gs.drawImage(image, null, null);

            ImageIO.write(rotatedImage, "JPEG", imageFile);
            return true;
        } catch (IOException e) {
            log.error("图片旋转失败", e);
        }
        return false;
    }

    public static BufferedImage rotateImage(File imageFile, int degree) {
        try {
            BufferedImage image = read(imageFile);
            int iw = image.getWidth();// 原始图象的宽度
            int ih = image.getHeight();// 原始图象的高度
            int w = 0;
            int h = 0;
            int x = 0;
            int y = 0;
            degree = degree % 360;
            if (degree < 0)
                degree = 360 + degree;// 将角度转换到0-360度之间
            double ang = Math.toRadians(degree);// 将角度转为弧度

            /**
             * 确定旋转后的图象的高度和宽度
             */

            if (degree == 180 || degree == 0 || degree == 360) {
                w = iw;
                h = ih;
            } else if (degree == 90 || degree == 270) {
                w = ih;
                h = iw;
            } else {
                double cosVal = Math.abs(Math.cos(ang));
                double sinVal = Math.abs(Math.sin(ang));
                w = (int) (sinVal * ih) + (int) (cosVal * iw);
                h = (int) (sinVal * iw) + (int) (cosVal * ih);
            }

            x = (w / 2) - (iw / 2);// 确定原点坐标
            y = (h / 2) - (ih / 2);
            BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
            Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();

            AffineTransform at = new AffineTransform();
            at.rotate(ang, w / 2, h / 2);// 旋转图象
            at.translate(x, y);
            gs.setTransform(at);
            gs.drawImage(image, null, null);

            // ImageIO.write(rotatedImage, "JPEG", imageFile);
            return rotatedImage;
        } catch (IOException e) {
            log.error("图片旋转失败", e);
        }
        return null;
    }

}
