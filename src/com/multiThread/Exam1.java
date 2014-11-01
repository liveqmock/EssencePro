package com.multiThread;

/**
 * 要求用子线程和主线程实现 子线程输出10次，主线程输出20次，然后再子线程输出10次，主线程输出20次，如此循环50次
 * 
 * @author 玄承勇
 * 
 */
public class Exam1 {

    public static void main(String[] args) {
        /**
         * 创建一个Business类的实例 为什么这儿需要使用final，不可使用static修饰？
         * final修饰后，表示此变量为只读，不可再次修改， 这样就保证了 内部类在引用变量的时候 与包含了自己的外部类一致
         * 而static修饰的变量，是属于类级别的变量，在类被第一次访问的时候就会创建并初始化
         */
        final Business business = new Business();
        Thread subThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    business.sub(i);
                }
            }
        });

        subThread.start();

        for (int i = 0; i < 50; i++) {
            business.main(i);
        }
    }

}
