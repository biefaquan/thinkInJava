package com.concurrent.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * P733
 * 对象池，管理数量有限的对象，当需要使用他们的时候签出他们，而在使用完毕后，可以将他们签回
 */
public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore available;
    public Pool(Class<T> classObject, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size, true);
        //Load pool with objects that can be checked out
        for (int i = 0; i < size; ++i) {
            try {
                //Assumes a default constructor;
                items.add(classObject.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
