package com.concurrent.example;

import java.util.concurrent.TimeUnit;

/**
 * P701
 * 中断状态被设置时，被阻塞和不被阻塞的各种可能
 */

class NeedsCleanup {
    private final int id;
    public NeedsCleanup (int ident) {
        id = ident;
        System.out.println("NeedsCleanup " + id);
    }

    public void cleanup() {
        System.out.println("Cleaning up " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double d = 0.0;

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()) {
                //point1
                NeedsCleanup n1 = new NeedsCleanup( 1);
                //start try-finally immediately after definition
                //of n1, to guarantee proper cleanup of nq;
                try {
                    System.out.println("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    // point2
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    // Guarantee proper cleanup of n2
                    try {
                        System.out.println("Calculating");
                        // A time-consuming, non-blocking operaton
                        for (int i = 1; i < 2500000; i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("Finished time-consuming operation");
                    } finally {
                        n2.cleanup();
                    }
                }finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        }
    }
}

public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        if(args.length != 1) {
            System.out.println("usage : java InterruptingIdiom delay-in-mS");
            System.exit(1);
        }
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(new Integer(args[0]));
        t.interrupt();
    }
}
