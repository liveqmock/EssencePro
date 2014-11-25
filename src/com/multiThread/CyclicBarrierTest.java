package com.multiThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * CyclicBarrier用于Java中对于线程的计数，CyclicBarrier在创建的时候，设置一个数目，用于计数，不满足此计数时，进入的线程处于等待状态，一旦满足此计数条件，则唤醒所有等待中的线程，继续向下执行。
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 创建一个计数器为3的CyclicBarrier对象
        final CyclicBarrier cb = new CyclicBarrier(10);

        System.out.println("========开始测试========");
        // 将三个任务添加到线程池中
        for (int i = 0; i < 10; i++) {
            Runnable temp = new Runnable() {
                @Override
                public void run() {
                    try {
                        // cb.getNumberWaiting() 当前等待的线程数
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("即将到达集合地点1，线程：" + Thread.currentThread().getName() + "已经进入。当前有" + (cb.getNumberWaiting() + 1)
                                + "个线程处于等候状态。" + ((cb.getNumberWaiting() == 9) ? "到齐啦 我们继续向前执行..." : ""));

                        // 在未达到计数前 处于等待状态 一旦达到计数 则唤醒所有等待中的线程 继续向下执行
                        cb.await();

                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("即将到达集合地点2，线程：" + Thread.currentThread().getName() + "已经进入。当前有" + (cb.getNumberWaiting() + 1)
                                + "个线程处于等候状态。" + ((cb.getNumberWaiting() == 9) ? "到齐啦 我们继续向前执行..." : ""));
                        cb.await();

                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("即将到达集合地点3，线程：" + Thread.currentThread().getName() + "已经进入。当前有" + (cb.getNumberWaiting() + 1)
                                + "个线程处于等候状态。" + ((cb.getNumberWaiting() == 9) ? "到齐啦 我们继续向前执行..." : ""));
                        cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };

            threadPool.execute(temp);
        }

        threadPool.shutdown();

        while (true) {
            if (threadPool.isTerminated()) {
                System.out.println("========测试完成========");
                break;
            }
        }
    }
}
