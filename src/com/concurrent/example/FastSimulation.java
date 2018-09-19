package com.concurrent.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * P760
 * 采用atomic来应用性能调优（其实就是替换效率低的synchronize）
 * 如果在计算过程中产生冲突，那么发现冲突的任务将直接忽略它，并不会更新它的值
 */
public class FastSimulation {
    static final int N_ELEMENTS = 100000;
    static final int N_GENES = 30;
    static final int N_EVOLVERS = 50;
    static final AtomicInteger[][]GRID = new AtomicInteger[N_ELEMENTS][N_GENES];
    static Random rand = new Random(47);
    static class Evolver implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                //Randomly select an element to work on
                int element = rand.nextInt(N_ELEMENTS);
                for (int i = 0; i < N_GENES; i++) {
                    int previous = element - 1;
                    if (previous < 0) {
                        previous = N_ELEMENTS - 1;
                    }
                    int next = element + 1;
                    if (next >= N_ELEMENTS) {
                        next = 0;
                    }
//                    int oldvalue =
                }
            }
        }
    }
}