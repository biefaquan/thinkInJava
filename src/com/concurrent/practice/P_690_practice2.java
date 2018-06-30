package com.concurrent.practice;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * P690 练习2
 */
class test2{
    Lock lock = new ReentrantLock();
    public void f(){
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("f()");
                Thread.yield();
            }
        }finally {
            lock.unlock();
        }
    }

    public void g() {
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }finally {
            lock.unlock();
        }
    }

    public void h() {
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("h()");
                Thread.yield();
            }
        }finally {
            lock.unlock();
        }
    }
}

public class P_690_practice2 {
    public static void main(String[] args) {
        final test2 t = new test2();
        new Thread(() -> t.f()).start();
        new Thread(() -> t.g()).start();
        t.h();
    }
}
