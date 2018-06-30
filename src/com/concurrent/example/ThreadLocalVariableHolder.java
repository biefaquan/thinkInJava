package com.concurrent.example;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P690
 * 采用ThreadLocal进行线程本地存储（可以使线程间的数据隔离）
 * ThreadLocal的实现原理？？  底层是一个map存储的。
 *
 */

class Accessor implements Runnable {
    private final int id;
    public Accessor(int idn) {
        this.id = idn;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            ThreadLocalVariableHolder.increment();
            System.out.println(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id + ": " +ThreadLocalVariableHolder.get();
    }
}

public class ThreadLocalVariableHolder {
    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private Random rand = new Random(47);
        @Override
        protected synchronized Integer initialValue() {
            return rand.nextInt(10000);
        }
    };
    public static void increment() {
        value.set(value.get() + 1);
    }
    public static int get() {
        return value.get();
    }

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(3);
        exec.shutdown();
    }
}
