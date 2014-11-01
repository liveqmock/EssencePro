package com.util.ip;

/**
 * 
 * @category 用来封装ip相关信息，目前只有两个字段，ip所在的国家和地区
 */

public class IPLocation {
    private String area;
    private String address;

    public IPLocation() {
        area = address = "";
    }

    public IPLocation getCopy() {
        IPLocation ret = new IPLocation();
        ret.area = area;
        ret.address = address;
        return ret;
    }

    public String getArea() {
        return area;
    }

    public void setCountry(String country) {
        this.area = country;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String area) {
        // 如果为局域网，纯真IP地址库的地区会显示CZ88.NET,这里把它去掉
        if (area.trim().equals("CZ88.NET")) {
            this.address = "本机或本网络";
        } else {
            this.address = area;
        }
    }
}
