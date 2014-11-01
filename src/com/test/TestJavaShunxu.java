package com.test;

/**
 * 测试java静态块、静态变量、非静态块。成员变量、方法运行顺序。 结果：静态库——静态变量——非静态块——成员变量——方法
 * 
 * @author 玄承勇
 * 
 */
public class TestJavaShunxu {
    static int num = 4; // 静态变量第二个
    {
        num += 3;
        System.out.println("b");
    }
    int a = 5;
    { // 成员变量第三个
        System.out.println("c");
    }

    TestJavaShunxu() { // 类的构造函数，第四个加载
        System.out.println("d");
    }

    static { // 静态块，第一个加载
        System.out.println("a");
    }

    static void run() // 静态方法，调用的时候才加载// 注意看，e没有加载
    {
        System.out.println("e");
    }

    public static void main(String[] args) {
        new TestJavaShunxu();
    }
}
