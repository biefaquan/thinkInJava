package com.concurrent.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * P688
 * Lock显示创建临界区
 * 但是线程确实不安全的了，报错？
 * 可能原因：ExplicitPairManager2.lock.lock()加的锁，与PairManager.getgetPair()锁的对象不是一个，
 * 所以在p.incrementX()的时候，getPair()执行，此时x!=y
 *
 */
class ExplicitPairManager1 extends PairManager {
    private Lock lock = new ReentrantLock();
    public synchronized void increment() {
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            store(getPair());
        }finally {
            lock.unlock();
        }
    }
}

class ExplicitPairManager2 extends PairManager {
    private Lock lock = new ReentrantLock();
    public void increment() {
        Pair temp;
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            System.out.println("x: " + p.getX() + "y: " + p.getY());
            temp = getPair();
        }finally {
            lock.unlock();
        }
        store(temp);
    }
}

public class ExplicitCriticalSection {
    public static void main(String[] args) {
        PairManager
                pman1 = new ExplicitPairManager1(),
                pman2 = new ExplicitPairManager2();
        CriticalSection.testApproaches(pman1, pman2);
    }
}
