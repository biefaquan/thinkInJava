package com.concurrent.example;

/**
 * P734
 * 一种创建代价高昂的对象类型
 */
public class Fat {
    private volatile double d;  // Prevent optimization
    private static int counter = 0;
    private final int id = counter++;
    public Fat() {
        //Expensive, interruptible operation
        for (int i = 0; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double)i;
        }
    }
    public void operation() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Fat id: " + id;
    }
}
