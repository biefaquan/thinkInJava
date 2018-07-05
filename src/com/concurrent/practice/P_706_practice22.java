package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P706
 * 创建一个忙等待的示例。
 * 碰见的问题： exec.shutdownNow()可以让睡眠等待的线程调用自己的interrupted()【例如r1,TimeUnit.MILLISECONDS.sleep(10)】，
 * 但是它无法让正在执行中的线程直接调用interrupted()【例如r2一直都在执行中，并未睡眠等待】，所以才会使用Thread.interrupted()去判断一下
 */


public class P_706_practice22 {
    private static volatile boolean flag = false;
    private static int spins;

    public static void main(String[] args) throws InterruptedException {
        Runnable r1 = () -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    flag = true;
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        Runnable r2 = () -> {
            while (true) {
                spins++;
                if (flag) {
                    flag = false;
                    System.out.println("Spun " + spins + " times");
                    spins = 0;
                }
                if(Thread.interrupted()) {
                    return;
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
