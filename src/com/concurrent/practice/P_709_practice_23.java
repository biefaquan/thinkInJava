package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P709
 * 使用notify()代替notify()时，WaxOnMatic.java可以成功地工作
 */
class CarP {
    private boolean waxOn = false;
    public synchronized void waxed() {
        waxOn = true;   //ready to buff
        notify();
    }
    public synchronized void buffed() {
        waxOn = false;  //ready for coat of wax
        notify();
    }
    public synchronized void waitForWaxing() throws InterruptedException {
        while (waxOn == false) {
            wait();
        }
    }
    public synchronized void waitForBuffing() throws InterruptedException {
        while (waxOn == true) {
            wait();
        }
    }
}

class WaxOnP implements Runnable {
    private CarP car;
    public WaxOnP(CarP c) {
        car = c;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                System.out.println("Wax On!");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax On task");
    }
}

class WaxOffP implements Runnable {
    private CarP car;
    public WaxOffP(CarP c) {
        car = c;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                car.waitForWaxing();
                System.out.println("Wax off! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax Off task");
    }
}

public class P_709_practice_23 {
    public static void main(String[] args) throws InterruptedException {
        CarP car = new CarP();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOffP(car));
        exec.execute(new WaxOnP(car));
        TimeUnit.SECONDS.sleep(5);//run for a while....
        exec.shutdownNow();    //interrupted all tasks
    }
}
