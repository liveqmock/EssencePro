package com.doubleBufferList;

import java.util.concurrent.LinkedBlockingQueue;

public class Tools {
    // public static List<Toy> lT = new ArrayList<Toy>(10000);
    // 或者采用阻塞队列。不用考虑同步问题。
    public static LinkedBlockingQueue<Toy> lT = new LinkedBlockingQueue<Toy>(1000);
    // public static List<Toy> lP = new ArrayList<Toy>(1000000);
}
