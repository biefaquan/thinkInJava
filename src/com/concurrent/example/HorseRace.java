package com.concurrent.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * P724
 * CyclicBarrier，可以理解为CountDownLatch的多次使用，
 * CyclicBarrier.await()执行的时候就是启动该类中的线程，然后阻塞等待该线程执行完
 */
class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter ++;
    private int strides = 0;
    private static Random random = new Random(47);
    private static CyclicBarrier barrier;
    public Horse(CyclicBarrier b) {
        barrier = b;
    }
    public synchronized int getStrides() {
        return strides;
    }
    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                synchronized (this) {
                    strides += random.nextInt(3);//Produces 0, 1, or 2
                }
                barrier.await();
            }
        } catch (InterruptedException e) {
            //A legitimate way to exit
        } catch (BrokenBarrierException e) {
            //This one we want to konw about
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Horse " + id + " ";
    }
    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}
public class HorseRace {
    static final int FINISH_SIZE = 75;
    private List<Horse> horses = new ArrayList<Horse>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public HorseRace(int nHorses, final int pause) {
        barrier = new CyclicBarrier(nHorses, new Runnable() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < FINISH_SIZE; i++) {
                    s.append("=");//The fence on the racetrack
                }
                System.out.println(s);
                for (Horse horse : horses) {
                    System.out.println(horse.tracks());
                }
                for (Horse horse : horses) {
                    if (horse.getStrides() >= FINISH_SIZE) {
                        System.out.println(horse + " won!!");
                        exec.shutdownNow();
                        return;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    System.out.println("barrier-action sleep interrupted");
                }
            }
        });
        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 200;
        if (args.length > 0) {//Optional argument
            int n = new Integer(args[0]);
            nHorses = n > 0 ? n : nHorses;
        }
        if (args.length > 1) {//Optional argument
            int p =new Integer(args[1]);
            pause = p > 0 ? p : pause;
        }
        new HorseRace(nHorses, pause);
    }
}
