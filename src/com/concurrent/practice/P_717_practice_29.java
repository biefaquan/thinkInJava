package com.concurrent.practice;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * P717
 * 修改ToastOMatic，使用两个单独的组装线来创建涂有花生黄油和果酱的吐司三明治（一个用于花生黄油，第二个用于果酱，然后把两条线合并）
 */
class Toast {
    public enum Status {DRY, BUTTERED, JAMMED,
        READY {
            @Override
            public String toString() {
                return
                        BUTTERED.toString() + " & " + JAMMED.toString();
            }
        }
    };
    private Status status = Status.DRY;
    private final int id;
    public Toast(int idn) {
        id = idn;
    }
    public void butter() {
        status = (status == Status.DRY) ? Status.BUTTERED : Status.READY;
    }
    public void jam() {
        status = (status == Status.DRY) ? Status.JAMMED : Status.READY;
    }
    public Status getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return "Toast " + id + ": " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rand = new Random(47);
    public Toaster(ToastQueue tq) {
        toastQueue = tq;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                //Make toast
                Toast t = new Toast(count++);
                System.out.println(t);
                //Insert into queue
                toastQueue.put(t);
            }
        } catch (Exception e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

//Apply butter to toast
class Butterer implements Runnable {
    private ToastQueue dryQueue, butteredQueue;
    public Butterer(ToastQueue dry, ToastQueue buttered) {
        dryQueue = dry;
        butteredQueue = buttered;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = dryQueue.take();
                t.butter();
                System.out.println(t);
                butteredQueue.put(t);
            }
        } catch (Exception e) {
            System.out.println("Butterer interrupted");
        }
        System.out.println("Butterer off");
    }
}

//Apply jam to buttered toast
class Jammer implements Runnable {
    private ToastQueue butteredQueue, finishedQueue;
    public Jammer(ToastQueue buttered, ToastQueue finished) {
        butteredQueue = buttered;
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = butteredQueue.take();
                t.jam();
                System.out.println(t);
                finishedQueue.put(t);
            }
        } catch (Exception e) {
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}

//Comsume the toast
class Eater implements Runnable {
    private ToastQueue finishedQueue;
    private int counter = 0;
    public Eater(ToastQueue finished) {
        finishedQueue = finished;
    }
    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = finishedQueue.take();
                //Verify that the toast is coming in order,
                //and that all pieces are getting jammed;
                if(t.getId() != counter++ || t.getStatus() != Toast.Status.JAMMED) {
                    System.out.println(">>>> Error: " + t);
                    System.exit(1);
                } else {
                    System.out.println("Chomp! " + t);
                }
            }
        } catch (Exception e) {
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}

//Outputs alternate inputs on alternate channels
class Alternator implements Runnable {
    private ToastQueue inQueue, out1Queue, out2Queue;
    private boolean outTo2;//control alternation
    public Alternator(ToastQueue in, ToastQueue out1, ToastQueue out2) {
        inQueue = in;
        out1Queue = out1;
        out2Queue = out2;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = inQueue.take();
                if (!outTo2) {
                    out1Queue.put(t);
                } else {
                    out2Queue.put(t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Alternator interrupted");
        }
        System.out.println("Alternator off");
    }
}

//Accepts toasts on either channel, and relays them on to
//a "single" sucessor
class Merger implements Runnable {
    private ToastQueue in1Queue, in2Queue, toBeButteredQueue,
                        toBeJammedQueue, finishedQueue;
    public Merger(ToastQueue in1, ToastQueue in2, ToastQueue toBeButtered,
                  ToastQueue toBeJammed, ToastQueue finished) {
        in1Queue = in1;
        in2Queue = in2;
        toBeButteredQueue = toBeButtered;
        toBeJammedQueue = toBeJammed;
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = null;
                while (t == null) {
                    t = in1Queue.poll(50, TimeUnit.MILLISECONDS);
                    if(t == null) {
                        break;
                    }
                    t = in2Queue.poll(50, TimeUnit.MILLISECONDS);
                }
                //Relay toast onto the proper queue
                switch (t.getStatus()) {
                    case BUTTERED:
                        toBeJammedQueue.put(t);
                        break;
                    case JAMMED:
                        toBeButteredQueue.put(t);
                        break;
                    default:
                        finishedQueue.put(t);
                }
            }
        } catch (InterruptedException e){
            System.out.println("Merger interrupted");
        }
        System.out.println("Merger off");
    }
}

public class P_717_practice_29 {
    public static void main(String[] args) throws Exception {
        ToastQueue
                dryQueue = new ToastQueue(),
                butteredQueue  = new ToastQueue(),
                toBeButteredQueue = new ToastQueue(),
                jammedQueue = new ToastQueue(),
                toBeJammedQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Alternator(dryQueue, toBeButteredQueue, toBeJammedQueue));
        exec.execute(new Butterer(toBeButteredQueue, butteredQueue));
        exec.execute(new Jammer(toBeJammedQueue, jammedQueue));
        exec.execute(new Merger(butteredQueue, jammedQueue, toBeButteredQueue, toBeJammedQueue, finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
