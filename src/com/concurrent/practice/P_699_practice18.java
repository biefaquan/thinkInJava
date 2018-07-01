package com.concurrent.practice;

import java.util.concurrent.TimeUnit;

/**
 * P699 练习18
 * 调用interrupt()终止任务，并确保这个任务被安全的关闭
 */
class TestSleep {
    public static void invokeSleep() {
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
    }
}

public class P_699_practice18 implements Runnable {
    @Override
    public void run() {
        TestSleep.invokeSleep();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new P_699_practice18());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        t.interrupt();
    }
}
