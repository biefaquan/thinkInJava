package com.concurrent.practice;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P711
 * 使用wait()和notifyAll()解决单个生产者、单个消费者问题
 */
class FlowQueue<T> {
    private Queue<T> queue = new LinkedList<T>();
    private int maxSize;
    public FlowQueue(int maxSize) {
        this.maxSize = maxSize;
    }
    public synchronized void put(T v) throws InterruptedException {
        while (queue.size() > maxSize) {
            wait();
        }
        queue.offer(v);
        maxSize ++;
        notifyAll();
    }
    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T returnValue = queue.poll();
        maxSize --;
        notifyAll();
        return  returnValue;
    }
}

class Item {
    private static int counter;
    private int id = counter ++;
    @Override
    public String toString() {
        return "Item " + id;
    }
}

//produces Item objects
class Producer implements Runnable {
    private int delay;
    private FlowQueue<Item> output;
    public Producer(FlowQueue<Item> output, int sleepTime) {
        this.output = output;
        delay = sleepTime;
    }

    @Override
    public void run() {
        for (;;) {
            try{
                output.put(new Item());
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

//consumes any object
class Consumer implements Runnable {
    private int delay;
    private FlowQueue<?> input;
    public Consumer(FlowQueue<?> input, int sleepTime) {
        this.input = input;
        delay = sleepTime;
    }

    @Override
    public void run() {
        for (;;) {
            try{
                System.out.println(input.get());
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

public class P_711_practice_24 {
    public static void main(String[] args) throws Exception{
        int producerSleep = 10;
        int consumerSleep = 10;
        FlowQueue<Item> fq = new FlowQueue<Item>(100);
        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.execute(new Producer(fq, producerSleep));
        exec.execute(new Consumer(fq, consumerSleep));
        TimeUnit.SECONDS.sleep(2);
        exec.shutdownNow();
    }
}
