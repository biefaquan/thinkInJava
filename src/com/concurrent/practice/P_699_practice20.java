package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P699 练习20
 * 修改CachedThreadPool，使得所有任务在结束前都收到一个interrupt()
 */
class Liftoff2 implements Runnable {
    public int countDown = 5000;
    private static int taskCount = 0;
    private final int id = taskCount++;
    public Liftoff2() {}
    public Liftoff2(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "Liftoff!") + ")	";
    }

    @Override
    public void run() {
        while(countDown-- > 0) {
            if(Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted: #" + id);
                return;
            }
            System.out.print(status());
            Thread.yield();
        }
    }
}

public class P_699_practice20 {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Liftoff2());
        }
        exec.shutdownNow();
    }
}
