package com.util.ip;

public class IPByteArray {

    private byte[] byteArray;

    public IPByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public void read(int position, byte[] bytes) {
        int p = position;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = read(p);
            p++;
        }
    }

    public byte read(int position) {
        return byteArray[position];
    }

}
