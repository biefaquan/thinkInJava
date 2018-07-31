package com.concurrent.practice;

/**
 * Created by bfq on 2018/7/18
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * P709
 * 厨师和服务员(比较经典)
 * 显示使用Lock和Condition对象
 */

class Meal3 {
    private final int orderNum;

    public Meal3(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toStirng() {
        return "Meal " + orderNum;
    }
}

class WaitPerson3 implements Runnable {
    private P_713_practice_27 restaurant;
    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public WaitPerson3(P_713_practice_27 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                lock.lock();
                try {
                    while (restaurant.meal == null) {
                        condition.await(); //... for the chef to produce a meal
                    }
                } finally {
                    lock.unlock();
                }
                System.out.println("Waitperson got " + restaurant.meal);
                restaurant.chef.lock.lock();
                try {
                    restaurant.meal = null;
                    restaurant.chef.condition.signalAll();// Ready for another
                } finally {
                    restaurant.chef.lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}

class Chef3 implements Runnable {
    private P_713_practice_27 restaurant;
    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();
    private int count = 0;

    public Chef3(P_713_practice_27 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                lock.lock();
                try {
                    while (restaurant.meal != null) {
                        condition.await(); //... for the meal to taken
                    }
                } finally {
                    lock.lock();
                }
                if (++count == 10) {
                    System.out.println("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Order up! ");
                restaurant.waitPerson.lock.lock();
                try {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.condition.signalAll();
                } finally {
                    restaurant.waitPerson.lock.unlock();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}

public class P_713_practice_27 {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson3 waitPerson = new WaitPerson3(this);
    Chef3 chef = new Chef3(this);

    public P_713_practice_27() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        new P_713_practice_27();
    }
}
