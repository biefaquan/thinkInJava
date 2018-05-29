package com.concurrent.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P672
 */
public class ExceptionThread implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceptionThread());
    }
}
