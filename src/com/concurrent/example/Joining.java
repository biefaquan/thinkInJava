package com.concurrent.example;

/**
 * P670
 * 通过调用t.join()，将另一个线程t加入当前执行线程中，当t线程执行完之后
 * （（或者在join方法中添加一个超时参数，时间到了t线程还没结束，join()方法总能返回））才会调用当前线程线程，
 * 也可通过调用interrupt()方法
 */
class Sleeper extends  Thread {
    private int duration;
    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted. "
            + "isInterrupted(): " + isInterrupted());
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread{
    private Sleeper sleeper;
    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}

public class Joining {
    public static void main(String[] args) {
        Sleeper sleepy = new Sleeper("Sleepy", 1500),
                grumpy = new Sleeper("Grumpy", 1500);
        Joiner dopey = new Joiner("Dopey", sleepy),
                doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}
