package com.concurrent.example;

import java.util.concurrent.TimeUnit;

/**
 * P664
 * 父线程被设置成后台线程，则它下面的子线程都被隐式设置成了后台线程
 */
class Daemon implements Runnable {
    private Thread[] t = new Thread[10];
    @Override
    public void run() {
        for (int i = 0; i< t.length; i++) {
            t[i] = new Thread(new DaemonSpawn());
            t[i].start();
            System.out.println("DaemonSpawn " + i + " started, ");
        }
        for (int i = 0; i< t.length; i++) {
            System.out.println("t[" + i + "].isDaemon() = " + t[i].isDaemon() + ", ");
        }
        while (true) {
            Thread.yield();
        }
    }
}

class DaemonSpawn implements  Runnable{
    @Override
    public void run() {
        while(true) {
            Thread.yield();
        }
    }
}

public class Daemons {
    public static void main(String[] args) throws InterruptedException {
        Thread d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();
        System.out.print("d.isDaemon() = " + d.isDaemon() + ", ");
        TimeUnit.SECONDS.sleep(1);
    }
}
