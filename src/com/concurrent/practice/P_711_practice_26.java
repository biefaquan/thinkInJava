package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P711
 * 向Restaurant中添加一个BusBuy类，在上菜之后，WaitPerson应该通知BusBoy清理
 */

class Meal {
    private final int orderNum;
    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }
    public String toStirng() {
        return "Meal " + orderNum;
    }
}

class WaitPerson implements Runnable {
    private P_711_practice_26 restaurant;
    public WaitPerson(P_711_practice_26 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal == null) {
                        wait(); //... for the chef to produce a meal
                    }
                }
                synchronized (restaurant.busBoy) {
                    restaurant.busBoy.meal = restaurant.meal;
                    restaurant.busBoy.notifyAll();
                }
                System.out.println("Waitperson got " + restaurant.meal);
                synchronized (restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll();// Ready for another
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}

class BusBoy implements Runnable {
    private P_711_practice_26 restaurant;
    volatile Meal meal;
    public BusBoy(P_711_practice_26 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (meal == null) {
                        wait(); //... for the chef to taken
                    }
                }
                synchronized (restaurant.waitPerson) {
                    restaurant.busBoy = null;
                    System.out.println("BusBoy got!");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("BusBoy interrupted");
        }
    }
}

class Chef implements Runnable {
    private P_711_practice_26 restaurant;
    private int count = 0;
    public Chef(P_711_practice_26 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal != null) {
                        wait(); //... for the meal to taken
                    }
                }
                if (++count == 10) {
                    System.out.println("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Order up! ");
                synchronized (restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}

public class P_711_practice_26 {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    BusBoy busBoy = new BusBoy(this);
    public P_711_practice_26 () {
        exec.execute(chef);
        exec.execute(waitPerson);
        exec.execute(busBoy);
    }

    public static void main(String[] args) {
        new P_711_practice_26();
    }
}
