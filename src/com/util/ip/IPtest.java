package com.util.ip;

public class IPtest {

    public static void main(String[] args) {
        // 指定纯真数据库的文件名，所在文件夹
        IPUtil ip = IPUtil.getInstance();
        // 测试IP 58.20.43.13
        System.out.println(ip.getIPLocation("58.20.43.13").getArea() + ":" + ip.getIPLocation("58.20.43.13").getAddress());

        String sip[] = { "60.177.69.138", "124.91.62.239", "124.91.62.239", "59.40.87.8", "221.222.73.88", "60.51.114.120",
                "222.212.223.146", "60.51.114.120", "60.51.114.120", "60.51.114.120", "60.51.114.120", "211.99.23.121", "58.45.85.107",
                "58.45.85.107", "121.32.94.204", "117.22.46.178", "218.19.2.195", "58.218.23.10", "58.218.23.10", "202.173.10.2",
                "60.178.167.241", "58.213.137.5", "58.38.165.221", "58.38.165.221", "61.150.15.25", "58.45.82.76", "218.8.200.210",
                "59.33.24.233", "60.176.240.196", "60.9.86.85", "60.51.114.120", "60.51.114.120", "125.120.155.45", "219.140.61.180",
                "219.140.61.180", "59.49.158.100", "59.49.158.100", "58.218.23.10", "221.220.25.54", "222.71.176.119", "220.168.115.124",
                "58.38.165.221", "218.3.251.246", "58.213.137.5", "218.3.251.246", "121.33.219.235", "222.130.195.225", "60.51.114.120",
                "218.3.251.246", "219.140.61.180", "219.140.61.180", "125.120.155.45", "60.9.97.186", "124.228.44.177", "218.19.2.195",
                "222.70.225.180", "58.38.165.221", "58.38.165.221", "60.168.67.99", "58.38.165.221", "58.38.165.221", "61.29.214.76",
                "220.234.156.254", "60.51.114.120", "60.51.114.120", "60.51.114.120", "222.168.148.111", "117.24.28.13", "59.49.158.100",
                "60.209.253.61", "61.150.15.83", "60.208.156.110", "124.227.192.131", "125.120.155.90", "218.83.58.250", "60.191.89.102",
                "222.71.176.119", "60.178.167.241", "60.208.111.203", "222.74.231.66", "58.221.253.118", "122.89.146.55", "60.51.114.120",
                "58.221.253.118", "59.49.158.100", "218.10.158.162", "220.160.38.98", "124.227.192.131", "219.245.44.214",
                "211.158.41.214", "219.245.44.185", "218.0.113.43", "211.158.41.214", "218.68.82.73", "125.120.144.14", "125.120.155.90",
                "60.171.169.251", "124.78.165.8", "124.78.165.8", "218.22.186.125" };
        for (int i = 0; i < sip.length; i++) {
            System.out.println(ip.getIPLocation(sip[i]).getArea() + " " + ip.getIPLocation(sip[i]).getAddress());
        }
    }
}
