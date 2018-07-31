package com.concurrent.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * P714
 * 使用同步队列解决任务协作问题
 */

class LiftOffRunner implements Runnable {
    private BlockingQueue<Liftoff> rockets;
    public LiftOffRunner(BlockingQueue<Liftoff> queue) {
        rockets = queue;
    }
    public void add(Liftoff lo) {
        try {
            rockets.add(lo);
        } catch (Exception e) {
            System.out.println("Interrupted during put()");
        }
        System.out.println("Exiting LiftOffRunner");
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Liftoff rocket = rockets.take();
                rocket.run();//use this thread
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

public class TestBlockingQueues {
    static void getkey() {
        try{
            // Compensate for Windows/Linux difference in the
            //length of the result produced by the Enter key;
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void getkey(String message) {
        System.out.println(message);
        getkey();
    }
    static void test(String msg, BlockingQueue<Liftoff> queue) {
        System.out.println(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++) {
            runner.add(new Liftoff(5));
        }
        getkey("Press 'Enter' (" + msg +")");
        t.interrupt();
        System.out.println("Finished " + msg + " test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue", new LinkedBlockingQueue<Liftoff>());//Unlimited size
        test("ArrayBlockingQueue", new ArrayBlockingQueue<Liftoff>(3));//Fixed size
        test("SynchronousQueue", new SynchronousQueue<Liftoff>());//Size of 1
    }
}
