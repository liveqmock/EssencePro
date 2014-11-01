package com.multiThread;

public class Business {
    boolean isSub = true; // 是否子线程

    /**
     * 子线程的输出方法
     * 
     * @param loop
     *            总的循环次数
     */
    public synchronized void sub(int loop) {
        while (!isSub) // 此处使用while更好 因为根据帮助文档 线程有时候没有被唤醒会自己醒来 被称之为伪唤醒
        {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("sub num:" + i + "#loop num:" + loop);
        }

        isSub = false;
        // 唤醒等待的线程
        this.notify();
    }

    /**
     * 主线程的输出方法
     * 
     * @param loop
     *            总的循环次数
     */
    public synchronized void main(int loop) {
        while (isSub) // 此处使用while更好 因为根据帮助文档 线程有时候没有被唤醒会自己醒来 被称之为伪唤醒
        {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 20; i++) {
            System.out.println("main num:" + i + "#loop num:" + loop);
        }

        isSub = true;
        // 唤醒等待的线程
        this.notify();
    }
}
