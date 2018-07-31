package com.concurrent.practice;

import com.concurrent.example.Liftoff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * P715
 * 修改TestBlockingQueue，添加一个LiftOff放置到BlockingQueue中的任务，而不要放置在main()中
 */
class LiftOffRunner1 implements Runnable {
    private BlockingQueue<Liftoff> rockets;
    public LiftOffRunner1(BlockingQueue<Liftoff> queue) {
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

class LiftOffProducer implements Runnable {
    private LiftOffRunner1 runner;
    public LiftOffProducer(LiftOffRunner1 runner) {
        this.runner = runner;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                runner.add(new Liftoff(5));
            }
        }catch (Exception e) {
            System.out.println("Waking for put()");
        }
        System.out.println("Exiting LiftOffProducer");
    }
}

public class P_715_practice_28 {
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
        ExecutorService exec = Executors.newFixedThreadPool(2);
        System.out.println(msg);
        LiftOffRunner1 runner = new LiftOffRunner1(queue);
        exec.execute(runner);
        exec.execute(new LiftOffProducer(runner));
        getkey("Press 'Enter' (" + msg +")");
        exec.shutdownNow();
        System.out.println("Finished " + msg + " test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue", new LinkedBlockingQueue<Liftoff>());//Unlimited size
        test("ArrayBlockingQueue", new ArrayBlockingQueue<Liftoff>(3));//Fixed size
        test("SynchronousQueue", new SynchronousQueue<Liftoff>());//Size of 1
    }
}
