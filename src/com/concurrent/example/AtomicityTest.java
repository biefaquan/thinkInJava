package com.concurrent.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P682
 * 为什么在getValue前加上synchronized之后，此程序就不会退出了？？
 * 因为在方法前面加synchronized是加的类的对象锁，即是synchronized(this)。
 * 所以在此程序中，at.getValue()必须等evenIncrement()方法执行完释放锁之后，才会执行。
 *
 */
public class AtomicityTest implements Runnable {
    private int i = 0;
    public /* synchronized */ int getValue() {
            return i;
    }
    private synchronized void evenIncrement() {
        i ++;
        i ++;
    }

    @Override
    public void run() {
        while (true) {
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicityTest at = new AtomicityTest();
        exec.execute(at);
        while (true) {
            int val = at.getValue();
            if(val % 2 != 0) {
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
