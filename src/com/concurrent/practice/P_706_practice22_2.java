package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P706
 * 创建一个忙等待的示例,使用wait()。
 */



public class P_706_practice22_2 {
    private static volatile boolean flag = false;
    private static int spins;

    public static void main(String[] args) throws InterruptedException {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        synchronized (this) {
                            notify();
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (r1) {
                        try {
                            r1.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println("Cycled");
                }
            }
        };
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(r1);
        exec.execute(r2);
        TimeUnit.SECONDS.sleep(1);
        exec.shutdownNow();
    }
}
