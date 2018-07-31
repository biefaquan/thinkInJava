package com.concurrent.practice;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *  P722
 *  修改DeadlockingDiningPhilosop，使用筷子之后，把筷子放到一个筷笼里
 *  此种方法并不能消除死锁
 */
class Chopstick1 {
    private boolean taken = false;
    public synchronized void take() throws InterruptedException {
        while (taken) {
            wait();
        }
        taken = true;
    }
    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}

class ChopstickQueue extends LinkedBlockingQueue {}

class Philosopher1 implements Runnable {
    private ChopstickQueue queue;
    private final int id;
    private final int ponderFactor;
    private Random random = new Random(47);
    private void pause() throws InterruptedException {
        if(ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(ponderFactor * 250));
    }
    public Philosopher1(ChopstickQueue queue, int ident, int ponder) {
        this.queue = queue;
        id = ident;
        ponderFactor = ponder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " " + "thinking");
                pause();
                //Philosopher becomes hungry
                System.out.println(this + " " + "grabbing right");
                Chopstick1 c1 = (Chopstick1) queue.take();
                System.out.println(this + " " + "grabbing left");
                Chopstick1 c2 = (Chopstick1)queue.take();
                System.out.println(this + " " + "eating");
                pause();
                queue.put(c1);
                queue.put(c2);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " " + "exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}

public class P_722_prcatice_31 {
    public static void main(String[] args) throws Exception {
        ChopstickQueue queue = new ChopstickQueue();
        int ponder =  5;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        int size = 1;
        if(args.length > 1) {
            size = Integer.parseInt(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick1[] sticks = new Chopstick1[size];
        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick1();
            queue.put(sticks[i]);
        }
        for (int i = 0; i < size; i++) {
            exec.execute(new Philosopher1(queue, i, ponder));
        }
        if (args.length == 3 && args[2].equals("timeout")) {
            TimeUnit.SECONDS.sleep(5);
        } else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }
}
