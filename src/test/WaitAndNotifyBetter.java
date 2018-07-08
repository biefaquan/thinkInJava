package test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * P706
 * 测试错失的信号（优化之后，死锁消失）
 */
class T1_Better implements Runnable {
    private WaitAndNotify waitAndNotify;
    private AtomicBoolean bool;

    public T1_Better(AtomicBoolean bool, WaitAndNotify waitAndNotify) {
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

class T2_Better implements Runnable {
    private WaitAndNotify waitAndNotify;
    private AtomicBoolean bool;

    public T2_Better(AtomicBoolean bool, WaitAndNotify waitAndNotify) {
        this.bool = bool;
        this.waitAndNotify = waitAndNotify;
    }

    @Override
    public void run() {
        synchronized (waitAndNotify) {
            while (!bool.get()) {
                try {
                    Thread.yield();
                    System.out.println("wait start");
                    waitAndNotify.wait();
                    System.out.println("wait end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class WaitAndNotifyBetter {
    private static volatile AtomicBoolean bool = new AtomicBoolean(false);
    public static void main(String[] args) {
        WaitAndNotify waitAndNotify = new WaitAndNotify();
        Runnable t1 = new T1_Better(bool, waitAndNotify);
        Runnable t2 = new T2_Better(bool, waitAndNotify);
        new Thread(t2).start();
        new Thread(t1).start();
    }
}
