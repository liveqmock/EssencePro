package com.multiThread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程间的数据交换。只能两个线程之间交换数据。可以是String。类。甚至集合例如List。
 * 
 * @author 玄承勇
 * 
 */
public class Exam2 {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        final Exchanger<String> exchanger = new Exchanger<String>();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String val = "待交换数据1";
                    System.out.println("线程：" + Thread.currentThread().getName() + " 等待交换数据。");

                    Thread.sleep((long) (Math.random() * 10000));

                    String result = exchanger.exchange(val);
                    System.out.println("线程：" + Thread.currentThread().getName() + " 数据交换完成  新数据为：" + result);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String val = "待交换数据2";
                    System.out.println("线程：" + Thread.currentThread().getName() + " 等待交换数据。");

                    Thread.sleep((long) (Math.random() * 10000));

                    String result = exchanger.exchange(val);
                    System.out.println("线程：" + Thread.currentThread().getName() + " 数据交换完成  新数据为：" + result);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}
