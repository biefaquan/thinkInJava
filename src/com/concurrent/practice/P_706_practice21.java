package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P706
 * 创建两个Runnable。其中一个启动然后调用wait(),另外一个捕获第一个对象的引用，
 * 其run()方法在一定的秒数之后，为第一个任务调用notifyAll(),从而使第一个任务可以显示一条信息
 * 总结:wait()和notiffAll()外面必须包含着锁
 */

class Coop1 implements Runnable {
    public Coop1() {
        System.out.println("Constructed Coop1");
    }
    @Override
    public void run() {
        System.out.println("Coop1 waiting");
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Coop1 exited");
    }
}

class Coop2 implements Runnable {
    private Runnable other;
    public Coop2(Runnable other) {
        this.other = other;
        System.out.println("Constructed Coop2");
    }
    @Override
    public void run() {
        System.out.println("Coop2 notifyAll");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (other) {
            other.notifyAll();
        }
        System.out.println("Coop2 Exited");
    }
}

public class P_706_practice21 {
    public static void main(String[] args) {
        Runnable run1 = new Coop1(),
                run2 = new Coop2(run1);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(run1);
        exec.execute(run2);
        Thread.yield();
        exec.shutdown();
    }
}
