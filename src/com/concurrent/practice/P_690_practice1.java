package com.concurrent.practice;

/**
 * P690 练习1
 */
class test{
    private Object o1 = new Object();
    private Object o2 = new Object();
    private Object o3 = new Object();
    public void f(){
        synchronized(o1) {
            for (int i = 0; i < 5; i++) {
                System.out.println("f()");
                Thread.yield();
            }
        }
    }

    public void g() {
        synchronized(o2) {
            for (int i = 0; i < 5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }

    public void h() {
        synchronized(o3) {
            for (int i = 0; i < 5; i++) {
                System.out.println("h()");
                Thread.yield();
            }
        }
    }
}

public class P_690_practice1 {
    public static void main(String[] args) {
        final test t = new test();
        new Thread(() -> t.f()).start();
        new Thread(() -> t.g()).start();
        t.h();
    }
}
