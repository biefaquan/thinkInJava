package com.concurrent.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P683
 * 问题：为什么在run()方法中调用外部静态变量，和使用自己的临时变量会有不同的结果，线程安全被破坏了。
 * 解析：因为外部静变量是共享的，所以就存在这种情况：
 *      1、Thread1 set serial = 1
 *      2、Thread2 set serial = 2
 *      3、Thread1 put 2 into CircularSet.
 *      4、Thread2 found it duplicate and exit.
 */
class CircularSet {
    private int[] array;
    private int len;
    private int index = 0;
    public CircularSet (int size) {
        array = new int[size];
        len = size;
        for (int i = 0; i < size; i++) {
            array[i] = -1;
        }
    }

    public synchronized void add(int i ) {
        array[index] = i;
        index = ++index % len;
    }

    public synchronized boolean contains(int val) {
        for (int i = 0; i < len; i++) {
            if(array[i] == val) {
                return true;
            }
        }
        return false;
    }
}

public class SerialNumberChecker {
    private static final int SIZE = 10;
    private static CircularSet serials = new CircularSet(1000);
    private static ExecutorService exec = Executors.newCachedThreadPool();
    private static int serial;
    static class SerialChecker implements Runnable {
        @Override
        public void run() {
            while(true) {
                //int serial;
                synchronized (serials) {
                     serial = SerialNumberGenerator.nextSerialNumber();
                }
                if (serials.contains(serial)) {
                    System.out.println("Duplicate: " + serial);
                    System.exit(0);
                }
                System.out.println(serial);
                serials.add(serial);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new SerialChecker());
            if (args.length > 0) {
                TimeUnit.SECONDS.sleep(new Integer(args[0]));
                System.out.println("No duplicates detected");
                System.exit(0);
            }
        }
    }
}
