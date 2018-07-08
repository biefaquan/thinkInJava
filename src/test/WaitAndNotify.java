package test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * P706
 * 测试错失的信号（会发生死锁）
 */
class T1 implements Runnable {
    private WaitAndNotify waitAndNotify;
    private AtomicBoolean bool;

    public T1(AtomicBoolean bool, WaitAndNotify waitAndNotify) {
        this.bool = bool;
        this.waitAndNotify = waitAndNotify;
    }

    @Override
    public void run() {
        synchronized (waitAndNotify) {
            System.out.println("notify start");
            bool.set(true);
            waitAndNotify.notify();
            System.out.println("notify end");
        }
    }
}

class T2 implements Runnable {
    private WaitAndNotify waitAndNotify;
    private AtomicBoolean bool;

    public T2(AtomicBoolean bool, WaitAndNotify waitAndNotify) {
        this.bool = bool;
        this.waitAndNotify = waitAndNotify;
    }

    @Override
    public void run() {
        while (!bool.get()) {
            try {
                synchronized (waitAndNotify) {
                    System.out.println("wait start");
                    waitAndNotify.wait();
                    System.out.println("wait end");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class WaitAndNotify {
    private static volatile AtomicBoolean bool = new AtomicBoolean(false);
    public static void main(String[] args) throws InterruptedException {
        WaitAndNotify waitAndNotify = new WaitAndNotify();
        Runnable t1 = new T1(bool, waitAndNotify);
        Runnable t2 = new T2(bool, waitAndNotify);
        new Thread(t1).start();
        new Thread(t2).start();
    }
}
