package com.concurrent.example;

import com.util.Generator;

import java.util.List;
import java.util.concurrent.*;

/**
 * P736
 * Exchanger是在两个任务之间交换对象的栅栏（BasicGenerator写的方法应该是有问题，结果没有显示出来）
 */
 class ExchangerProducer<T> implements Runnable {
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    ExchangerProducer(Exchanger<List<T>> exchg,
                             Generator<T> gen, List<T> holder) {
        exchanger = exchg;
        generator = gen;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < ExchangerDemo.size; i++) {
                    holder.add(generator.next());
                    // Exchange full for empty;
                    holder = exchanger.exchange(holder);
                }
            }
        }catch (InterruptedException e) {
            // Ok to terminate this way
        }
    }
}

class ExchangerConsumer<T> implements Runnable {
    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    private volatile  T value;
    ExchangerConsumer(Exchanger<List<T>> ex, List<T> holder) {
        exchanger = ex;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                holder = exchanger.exchange(holder);
                for (T x : holder) {
                    value = x;//Fetch out value
                    holder.remove(x); // Ok for CopyOnWriteArrayList
                }
            }
        } catch (InterruptedException e) {
            // Ok to terminate this way
        }
        System.out.println("Final value : " + value);
    }
}

public class ExchangerDemo {
     static int size = 0;
     static int delay = 5; //seconds

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            size = new Integer(args[0]);
        }
        if (args.length > 1) {
            delay = new Integer(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> xc = new Exchanger<List<Fat>>();
        List<Fat>
                producerList = new CopyOnWriteArrayList<>(),
                consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducer<Fat>(xc, BasicGenerator.create(Fat.class), producerList));
        exec.execute(new ExchangerConsumer<Fat>(xc, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
    }
}
