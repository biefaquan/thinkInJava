package com.concurrent.practice;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/**
 * P718
 * 修改PipedIO.java，使用BlockQueue而不是管道
 */
class Sender implements Runnable {
    private Random rand = new Random(47);
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();
    public LinkedBlockingQueue getQueue() {
        return queue;
    }

    @Override
    public void run() {
        try{
            while (true) {
                for (char c = 'A'; c <= 'z'; c++) {
                    queue.put(c);
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Sender sleep interrupted");
        }
    }
}

class Receiver implements Runnable {
    private LinkedBlockingQueue queue;
    public Receiver(Sender sender) throws IOException {
        queue = sender.getQueue();
    }

    @Override
    public void run() {
        try {
            while (true) {
                //Blocks until characters are there;
                System.out.println("Read: " + (char)queue.take() + ",");
            }
        } catch (Exception e) {
            System.out.println(e + " Receiver read exception");
        }
    }
}

public class P_718_practice_30 {
    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}
