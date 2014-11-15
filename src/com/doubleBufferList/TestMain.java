package com.doubleBufferList;

public class TestMain {

    public static void main(String[] args) {
        Factory f = new Factory();
        f.start();
        Kid k = new Kid();
        k.start();
    }

}
