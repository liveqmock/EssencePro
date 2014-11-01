package com.util.ip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.util.WebUtil;

public class IPUtil {

    private final static Logger logger = Logger.getLogger(IPUtil.class);

    // 一些固定常量，比如记录长度等等
    private static final int IP_RECORD_LENGTH = 7;
    private static final byte REDIRECT_MODE_1 = 0x01;
    private static final byte REDIRECT_MODE_2 = 0x02;
    private static final String FILENAME = WebUtil.getProjectAbsolutePath() + "qqwry.dat";
    private static File file = new File(FILENAME);
    private static long lastmodifytime = file.lastModified();
    private static Timer loadTimer = new Timer(true);

    // 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
    private static ConcurrentHashMap<String, IPLocation> ipCache;

    // 起始地区的开始和结束的绝对偏移
    private static long ipBegin, ipEnd;

    private volatile static IPUtil instance = null;
    private static IPByteArray byteArray = null;

    /**
     * 构造函数，传入值为InputStream
     * 
     * @param inputStream
     * @return
     */
    public static IPUtil getInstance(InputStream inputStream) {
        if (null == instance) {
            synchronized (IPUtil.class) {
                if (null == instance) {
                    byteArray = new IPByteArray(IPLocationUtil.getByteArrayFromInputStream(inputStream));
                    instance = new IPUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数，传入文件默认Classpath下qqwry.dat
     * 
     * @param fileName
     * @return
     */
    public static IPUtil getInstance(String fileName) {
        if (null == instance) {
            synchronized (IPUtil.class) {
                if (null == instance) {
                    byteArray = new IPByteArray(IPLocationUtil.getByteArrayFromClasspathFile(fileName));
                    instance = new IPUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数
     * 
     * @param fileName
     * @return
     */
    public static IPUtil getInstance() {
        if (null == instance) {
            synchronized (IPUtil.class) {
                if (null == instance) {
                    byteArray = new IPByteArray(IPLocationUtil.getByteArrayFromClasspathFile(FILENAME));
                    instance = new IPUtil();
                    loadTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                long modifttime = file.lastModified();
                                if (modifttime != 0 && modifttime != lastmodifytime) {
                                    IPByteArray byteArray = new IPByteArray(IPLocationUtil.getByteArrayFromClasspathFile(FILENAME));
                                    IPUtil.byteArray = byteArray;
                                    ipBegin = readLong4(0);
                                    ipEnd = readLong4(4);

                                    ipCache.clear();
                                    lastmodifytime = modifttime;
                                    logger.info("更新ip库文件");
                                }
                            } catch (Exception e) {
                                logger.info("更新ip库文件出错！", e);
                            }
                        }
                    }, 100, 1000);
                }
            }
        }
        return instance;
    }

    private IPUtil() {
        ipCache = new ConcurrentHashMap<String, IPLocation>();
        ipBegin = readLong4(0);
        ipEnd = readLong4(4);
    }

    /**
     * 查询IP信息，返回IPLocation对象
     * 
     * @param ip
     * @return
     */
    public IPLocation getIPLocation(String ip) {
        byte[] ipByte = IPLocationUtil.getIpByteArrayFromString(ip);
        if (ipCache.containsKey(ip)) {
            IPLocation ipLoc = ipCache.get(ip);
            return ipLoc;
        } else {
            IPLocation ipLoc = getIPLocation(ipByte);
            ipCache.put(ip, ipLoc.getCopy());
            return ipLoc;
        }
    }

    /**
     * 根据ip搜索ip信息文件，得到IPLocation结构，所搜索的ip参数从类成员ip中得到
     * 
     * @param ip
     *            要查询的IP
     * @return IPLocation结构
     */
    private IPLocation getIPLocation(byte[] ip) {
        IPLocation info = null;
        long offset = locateIP(ip);
        if (offset != -1)
            info = getIPLocation(offset);
        if (info == null) {
            info = new IPLocation();
            info.setCountry(Message.unknown_country);
            info.setAddress(Message.unknown_area);
        }
        return info;
    }

    /**
     * 从offset位置读取4个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
     * 
     * @param offset
     * @return 读取的long值，返回-1表示读取文件失败
     */
    private static long readLong4(long offset) {
        byte[] byte4 = new byte[4];
        long ret = 0;
        try {
            byteArray.read((int) offset, byte4);
            ret |= (byte4[0] & 0xFF);
            ret |= ((byte4[1] << 8) & 0xFF00);
            ret |= ((byte4[2] << 16) & 0xFF0000);
            ret |= ((byte4[3] << 24) & 0xFF000000);
            return ret;
        } catch (Exception e) {
            logger.error("", e);
            return -1;
        }
    }

    /**
     * 从offset位置读取3个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
     * 
     * @param offset
     *            整数的起始偏移
     * @return 读取的long值，返回-1表示读取文件失败
     */
    private long readLong3(long offset) {
        byte[] b3 = new byte[3];
        long ret = 0;
        try {
            byteArray.read((int) offset, b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
     * 文件中是little-endian形式，将会进行转换
     * 
     * @param offset
     * @param ip
     */
    private void readIP(long offset, byte[] ip) {
        try {
            byteArray.read((int) offset, ip);
            byte temp = ip[0];
            ip[0] = ip[3];
            ip[3] = temp;
            temp = ip[1];
            ip[1] = ip[2];
            ip[2] = temp;
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 把类成员ip和beginIp比较，注意这个beginIp是big-endian的
     * 
     * @param ip
     *            要查询的IP
     * @param beginIp
     *            和被查询IP相比较的IP
     * @return 相等返回0，ip大于beginIp则返回1，小于返回-1。
     */
    private int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0)
                return r;
        }
        return 0;
    }

    /**
     * 把两个byte当作无符号数进行比较
     * 
     * @param b1
     * @param b2
     * @return 若b1大于b2则返回1，相等返回0，小于返回-1
     */
    private int compareByte(byte b1, byte b2) {
        if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
            return 1;
        else if ((b1 ^ b2) == 0)// 判断是否相等
            return 0;
        else
            return -1;
    }

    /**
     * 这个方法将根据ip的内容，定位到包含这个ip国家地区的记录处，返回一个绝对偏移 方法使用二分法查找。
     * 
     * @param ip
     *            要查询的IP
     * @return 如果找到了，返回结束IP的偏移，如果没有找到，返回-1
     */
    private long locateIP(byte[] ip) {
        byte[] b4 = new byte[4];
        long m = 0;
        int r;
        // 比较第一个ip项
        readIP(ipBegin, b4);
        r = compareIP(ip, b4);
        if (r == 0)
            return ipBegin;
        else if (r < 0)
            return -1;
        // 开始二分搜索
        for (long i = ipBegin, j = ipEnd; i < j;) {
            m = getMiddleOffset(i, j);
            readIP(m, b4);
            r = compareIP(ip, b4);
            if (r > 0)
                i = m;
            else if (r < 0) {
                if (m == j) {
                    j -= IP_RECORD_LENGTH;
                    m = j;
                } else
                    j = m;
            } else
                return readLong3(m + 4);
        }
        // 如果循环结束了，那么i和j必定是相等的，这个记录为最可能的记录，但是并非
        // 肯定就是，还要检查一下，如果是，就返回结束地址区的绝对偏移
        m = readLong3(m + 4);
        readIP(m, b4);
        r = compareIP(ip, b4);
        if (r <= 0)
            return m;
        else
            return -1;
    }

    /**
     * 得到begin偏移和end偏移中间位置记录的偏移
     * 
     * @param begin
     * @param end
     * @return
     */
    private long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / IP_RECORD_LENGTH;
        records >>= 1;
        if (records == 0)
            records = 1;
        return begin + records * IP_RECORD_LENGTH;
    }

    /**
     * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构
     * 
     * @param offset
     *            国家记录的起始偏移
     * @return IPLocation对象
     */
    private IPLocation getIPLocation(long offset) {
        try {
            IPLocation loc = new IPLocation();
            // 跳过4字节ip
            long position = offset + 4;

            // 读取第一个字节判断是否标志字节
            byte b = byteArray.read((int) position);
            position++;

            if (b == REDIRECT_MODE_1) {
                // 读取国家偏移
                long countryOffset = readLong3(position);
                // 跳转至偏移处
                position = countryOffset;
                // 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
                b = byteArray.read((int) position);
                position++;
                if (b == REDIRECT_MODE_2) {
                    readCountry(loc, readLong3(position));
                    readArea(loc, countryOffset + 4);
                } else {
                    long afterReadCountry = readCountry(loc, countryOffset);
                    readArea(loc, afterReadCountry);
                }
            } else if (b == REDIRECT_MODE_2) {
                readCountry(loc, readLong3(position));
                readArea(loc, offset + 8);
            } else {
                long afterReadCountry = readCountry(loc, position - 1);
                readArea(loc, afterReadCountry);
            }
            return loc;
        } catch (IOException e) {
            return null;
        }
    }

    private long readCountry(IPLocation loc, long offset) throws IOException {
        return readString(loc, (int) offset, 1);
    }

    private void readArea(IPLocation loc, long offset) throws IOException {
        byte b = byteArray.read((int) offset);
        if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
            long areaOffset = readLong3(offset + 1);
            if (areaOffset == 0) {
                loc.setAddress(Message.unknown_area);
            } else {
                readString(loc, (int) areaOffset, 2);
            }
        } else {
            readString(loc, (int) offset, 2);
        }
    }

    /**
     * 从position偏移处读取一个以0结束的字符串
     * 
     * @param loc
     * @param position
     *            字符串起始偏移
     * @param type
     * @return
     */
    private int readString(IPLocation loc, int position, int type) {
        int n = 0;
        byte[] temp = new byte[100];
        while (true) {
            if (byteArray.read(position) == 0) {
                break;
            } else {
                temp[n] = byteArray.read(position);
                position++;
                n++;
            }
            if (n >= temp.length) {
                byte[] tmp = new byte[n + 100];
                System.arraycopy(temp, 0, tmp, 0, n);
                temp = tmp;
            }
        }
        if (n != 0) {
            if (type == 1)
                loc.setCountry(IPLocationUtil.getString(temp, 0, n, "GBK"));
            else
                loc.setAddress(IPLocationUtil.getString(temp, 0, n, "GBK"));
        }
        return position + 1;
    }

}
