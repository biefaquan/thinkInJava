package com.concurrent.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P724
 * 修改装饰性花园，OrnamemtalGarden.java
 * 主要是通过CountDownLatch.await()替换掉exec.awaitTermination()方法，实现阻塞等待的效果
 */
class Entrance3 implements Runnable {
    private CountDownLatch latch;
    private static Count count = new Count();
    private static List<Entrance3> entrances = new ArrayList<Entrance3>();
    private int number = 0;
    private final int id;
    private static volatile boolean canceled = false;
    public static void cancel() {
        canceled = true;
    }
    public Entrance3(CountDownLatch latch, int id) {
        this.id = id;
        this.latch = latch;
        entrances.add(this);
    }

    public void run() {
        while (!canceled) {
            synchronized (this) {
                ++number;
            }
            System.out.println(this + " Total: " + count.increment());
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }
        }
        latch.countDown();
        System.out.println("Stopping " +this);
    }

    public synchronized int getValue() {
        return number;
    }

    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }

    public static int getTotalCount() {
        return count.value();
    }

    public static int sumEntrances() {
        int sum = 0;
        for (Entrance3 entrance : entrances) {
            sum += entrance.getValue();
        }
        return sum;
    }
}

public class P_724_practice_32 {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            exec.execute(new Entrance3(latch, i));
        }
        TimeUnit.SECONDS.sleep(3);
        Entrance3.cancel();
        exec.shutdown();
        latch.await();//Wait for results
        System.out.println("Total: " + Entrance3.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance3.sumEntrances());
    }
}
