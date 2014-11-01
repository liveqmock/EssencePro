package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.model.ConfigModel;

public class FileUtil {

    private final static Logger log = Logger.getLogger(FileUtil.class);

    private static long oldName = ConfigModel.MARK.getPropertyLongValue("oldFileName", 0);

    private static Lock lock = new ReentrantLock();

    /**
     * 将指定目录下的文件名字变为大写或小写：u--变为大写，l-变为小写
     * 
     * @param path
     * @param up
     *            u——大写，l——小写
     * @author 玄承勇
     * @date 2014-5-17 上午10:02:02
     */
    public static void changePathName(String path, String up) {
        File d = new File(path); // 取得当前文件夹下所有文件和目录的列表
        File lists[] = d.listFiles();
        String pathss = new String("");
        // 对当前目录下面所有文件进行检索
        for (int i = 0; i < lists.length; i++) {
            if (lists[i].isFile()) {
                String filename = lists[i].getName();
                if (up.equals("u")) {
                    filename = StringUtil.upCase(filename);
                } else {
                    filename = StringUtil.lowerCase(filename);
                }
                String toName = new String(path + filename);
                File tempf = new File(toName);
                lists[i].renameTo(tempf);
            } else {
                pathss = path;
                // 进入下一级目录
                pathss = pathss + lists[i].getName() + "\\";
                // 递归遍历所有目录
                changePathName(pathss, up);
            }
        }
    }

    /**
     * 得到文件大小MB，KB,B。
     * 
     * @param size
     * @return
     * @author 玄承勇
     * @date 2014-5-17 上午10:02:51
     */
    public static String getSoftsize(long size) {
        float f = 0f;
        String tmp = "";
        if (size > 1024 * 1024) {
            f = (float) size / 1024 / 1024;
            tmp = "MB";
        } else if (size > 1024) {
            f = (float) size / 1024;
            tmp = "KB";
        } else {
            f = (float) size;
            tmp = "B";
        }

        java.text.DecimalFormat df = new DecimalFormat(".##");
        tmp = (String) df.format(f) + tmp;
        return tmp;
    }

    /**
     * 获取文件中key的值。properties文件。
     * 
     * @param fileName
     * @param key
     * @return
     * @author 玄承勇
     * @date 2014-5-17 上午10:04:10
     */
    public static String getValueByKey(String filePath, String key) {
        String value = "";

        InputStream in = FileUtil.class.getResourceAsStream(filePath);

        Properties prop = new Properties();

        try {
            prop.load(in);
            in.close();
            value = prop.getProperty(key, "none");
        } catch (IOException e) {
            value = "none";
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 根据路径、后缀生成新的文件名
     * 
     * @param directory
     * @param suf
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午5:00:29
     */
    public static String getNewFileName(String directory, String suf) {
        return getNewFileName(directory, "", suf);
    }

    /**
     * 根据路径、前缀、后缀获取新的文件名
     * 
     * @param directory
     * @param prefix
     * @param suf
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午4:59:16
     */
    public static String getNewFileName(String directory, String prefix, String suf) {
        String path = DateUtil.getNowDateTime("yyyy/MM/dd/");
        if (!StringUtils.isEmpty(directory)) {
            path = directory + "/" + path;
        }
        String fileName = prefix + createNewFileName();
        return path + fileName + suf;
    }

    /**
     * 根据时间戳生成文件名
     * 
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午5:02:56
     */
    private static String createNewFileName() {
        try {
            lock.lock();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            long fileName = new Date().getTime() - c.getTime().getTime();
            if (fileName <= oldName) {
                fileName = oldName + 1;
            }
            oldName = fileName;
            // 写入文件中
            ConfigModel.MARK.writeProperty("oldFileName", oldName + "");
            return Long.toString(fileName, 36);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 上传文件
     * 
     * @param fileItem
     * @param typeDir
     *            上传路径
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-28 上午11:00:44
     */
    public static String uploadFile(FileItem fileItem, String typeDir) throws Exception {
        return saveFileItemAndGetPath(fileItem, typeDir);
    }

    /**
     * 上传文件
     * 
     * @param fileItem
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-27 下午5:06:10
     */
    public static String uploadFile(FileItem fileItem) throws Exception {
        return saveFileItemAndGetPath(fileItem, "");
    }

    /**
     * 保存文件后返回路径
     * 
     * @param fileItem
     * @param directory
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-27 下午5:06:58
     */
    private static String saveFileItemAndGetPath(FileItem fileItem, String directory) throws Exception {
        return saveFileItemAndGetPath(fileItem, directory, "");
    }

    /**
     * 保存文件后返回路径。
     * 
     * @param fileItem
     * @param directory
     * @param prefix
     *            文件名前缀
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-27 下午5:07:40
     */
    private static String saveFileItemAndGetPath(FileItem fileItem, String directory, String prefix) throws Exception {
        if (fileItem != null && !fileItem.getName().equalsIgnoreCase("")) {

            String name = fileItem.getName();
            /** 得到文件名后缀 */
            String suf = name.substring(name.lastIndexOf("."));

            String fileName = getNewFileName(directory, prefix, suf);
            File file = createFile(fileName);

            fileItem.write(file);
            return getRelativePath(file);
        }
        return null;
    }

    /**
     * 创建一个文件实体
     * 
     * @param fileName
     * @return
     * @throws IOException
     * @author 玄承勇
     * @date 2014-6-27 下午5:10:45
     */
    public static File createFile(String fileName) throws IOException {
        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    /**
     * 获取文件路径
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午5:14:16
     */
    private static String getFilePath(String fileName) {
        fileName = ConfigModel.CONFIG.getProperty("fileRootPath") + "/" + fileName;
        fileName = fileName.replace("\\", "/").replaceAll("/+", "/");
        return fileName;
    }

    /**
     * 获取文件的相对路径。
     * 
     * @param file
     * @return
     * @author 玄承勇
     * @date 2014-6-27 下午5:14:51
     */
    private static String getRelativePath(File file) {
        String fileRootPath = ConfigModel.CONFIG.getProperty("fileRootPath");
        return file.getAbsolutePath().replace("\\", "/").replace(fileRootPath, "");
    }

    /**
     * 上传附件
     * 
     * @param fileItem
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-28 上午11:03:31
     */
    public static String uploadAttr(FileItem fileItem) throws Exception {
        return saveFileItemAndGetPath(fileItem, "attr");
    }

    /**
     * 上传报告
     * 
     * @param fileItem
     * @param prefix
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-28 上午11:05:12
     */
    public static String uploadReport(FileItem fileItem, String prefix) throws Exception {
        return saveFileItemAndGetPath(fileItem, "reports", prefix);
    }

    /**
     * 上传图片
     * 
     * @param fileItem
     * @return
     * @throws Exception
     * @author 玄承勇
     * @date 2014-6-28 上午11:05:28
     */
    public static String uploadImg(FileItem fileItem) throws Exception {
        return saveFileItemAndGetPath(fileItem, "Img");
    }

    /**
     * 压缩文件
     * 
     * @param fileName
     *            压缩文件名
     * @param entryFileName
     *            实体文件名(zip里面的文件名)
     * @param content
     * @return
     * @throws IOException
     * @author 玄承勇
     * @date 2014-6-28 上午11:06:34
     */
    public static String saveZipFile(String fileName, String entryFileName, String content) throws IOException {
        File file = createFile(fileName);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
        try {
            zos.putNextEntry(new ZipEntry(entryFileName));
            zos.write(content.getBytes("utf-8"));
            zos.closeEntry();
            return getRelativePath(file);
        } finally {
            zos.flush();
            zos.close();
        }
    }

    /**
     * 把内容content写入到文件中
     * 
     * @param fileName
     * @param content
     * @return
     * @throws IOException
     * @author 玄承勇
     * @date 2014-6-28 上午11:10:16
     */
    public static String saveFile(String fileName, String content) throws IOException {
        File file = createFile(fileName);

        FileOutputStream out = new FileOutputStream(file);
        try {
            out.write(content.getBytes("utf-8"));
            return getRelativePath(file);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 得到图片的url
     * 
     * @param path
     * @return
     * @author 玄承勇
     * @date 2014-6-28 上午11:12:34
     */
    public static String getImageUrl(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        return ConfigModel.CONFIG.getProperty("imageHost") + path.replaceAll("http://(.*?)/", "").replaceAll("(?m)_mark$", "");
    }

    public static void main(String[] args) throws IOException {
        FileUtil.saveZipFile("c:/tt.zip", "t.txt", "我匀");
    }

    /**
     * 图片正则表达式
     * 
     * @return
     * @author 玄承勇
     * @date 2014-6-28 上午11:22:52
     */
    public static String getImageRegex() {
        return "<img.*?src=('|\")(" + ConfigModel.CONFIG.getProperty("imageHost") + ".*?)('|\").*?/?>";
    }

    /**
     * 得到图片的路径
     * 
     * @param url
     * @return
     * @author 玄承勇
     * @date 2014-6-28 上午11:23:37
     */
    public static String getImagePath(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        return url.replaceAll("http://(.*?)/", "");
    }

    /**
     * 删除文件
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-28 上午11:24:16
     */
    public static boolean delete(String fileName) {
        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件，关联上级文件。
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-28 上午11:37:04
     */
    public static boolean deleteCaseParent(String fileName) {
        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        if (file.exists()) {
            File parentFile = file.getParentFile();
            File[] files = parentFile.listFiles();
            if (files.length == 1) {
                file.delete();
                deleteEmptyParent(parentFile);
                return true;
            } else {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 删除无子文件的文件，如果文件的上级文件为根路径，则不删除。
     * 
     * @param file
     * @author 玄承勇
     * @date 2014-6-28 上午11:42:37
     */
    public static void deleteEmptyParent(File file) {
        File[] files = file.listFiles();
        if (files.length == 0) {
            File parentFile = file.getParentFile();
            if (getRelativePath(parentFile).equals("")) {
                return;
            }
            file.delete();
            deleteEmptyParent(parentFile);
        }
    }

    /**
     * 
     * 移动文件
     * 
     * @param fileName
     * @param newFileName
     * @return
     * @throws IOException
     * @author 玄承勇
     * @date 2014-6-28 上午11:50:52
     */
    public static boolean moveFile(String fileName, String newFileName) throws IOException {
        String filePath = getFilePath(fileName);
        String newFilePath = getFilePath(newFileName);
        File file = new File(filePath);
        File newfile = new File(newFilePath);
        if (file.exists()) {
            if (newfile.exists()) {
                newfile.delete();
            }
            FileUtils.moveFile(file, newfile);
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否存在。
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-28 下午12:02:57
     */
    public static boolean exists(String fileName) {
        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 获取文件
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-28 下午1:47:22
     */
    public static File getFile(String fileName) {
        String fileRootPath = ConfigModel.CONFIG.getProperty("fileRootPath");
        return getFile(fileRootPath, fileName);
    }

    /**
     * 获取文件
     * 
     * @param rootPath
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-6-28 下午1:47:39
     */
    public static File getFile(String rootPath, String fileName) {
        fileName = fileName.replace("\\", "/").replaceAll("/+", "/");
        File file = new File(rootPath, fileName);
        if (!file.exists()) {
            log.warn("文件没找到，rootPath:" + rootPath + ",fileName" + fileName);
            return null;
        }
        return file;
    }

    /**
     * 读取文件。每一行存一个String
     * 
     * @param fileName
     * @return
     * @author 玄承勇
     * @date 2014-9-4 下午2:27:36
     */
    public static List<String> getLines(String fileName) {
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

}
