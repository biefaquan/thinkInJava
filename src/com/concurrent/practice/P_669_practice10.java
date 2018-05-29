package com.concurrent.practice;

import com.util.Fibonacci;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * P656 练习2
 *
 * @author bfq
 */
public class P_669_practice10 {
    public static void main(String[] args) {
        ArrayList<Future<String>> result = new ArrayList<Future<String>>();
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            result.add(new ThreadMethod(5).runTask(exec));
        }
        for (Future<String> future : result) {
            try {
                System.out.println("Future value:" + future.get() + " end!!");
            } catch (InterruptedException e) {
                System.out.println(e);
                return;
            } catch (ExecutionException e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
        }
    }
}

class ThreadMethod {
    private int n = 1;
    private Callable<String> t;

    public ThreadMethod() {
    }

    public ThreadMethod(int n) {
        this.n = n;
    }

    public Future runTask(ExecutorService exec) {
        if (t == null) {
            t = new Callable<String>() {
                @Override
                public String call() {
                    Fibonacci gen = new Fibonacci();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < n; i++) {
                        sb.append(gen.next() + "\t");
                    }
                    Thread.yield();
                    return sb.toString();
                }

                public String toString() {
                    return Thread.currentThread().getName() + ":" + n;
                }
            };
            return exec.submit(t);
        }
        return null;
    }
}


