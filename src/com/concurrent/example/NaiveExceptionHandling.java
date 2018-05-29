package com.concurrent.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P673
 * 无法在main线程中捕获子线程中的异常
 */
public class NaiveExceptionHandling {
    public static void main(String[] args) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(new ExceptionThread());
        } catch (Exception e) {
            System.out.println("Exception has been handled");
        }
    }
}
