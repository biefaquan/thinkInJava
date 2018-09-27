package com.lambda.chapter5.answers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bfq on 2018/9/27
 */
public class Question3 {
    //使用 Map 的 computeIfAbsent 方法高效计算斐波那契数列。这里的“高效”是指避免将那些较小的序列重复计算多次。
    private final Map<Integer,Long> cache;

    public Question3() {
        cache = new HashMap<>();
        cache.put(0, 0L);
        cache.put(1, 1L);
    }

    public long fibonacci(int x) {
        return cache.computeIfAbsent(x, n -> fibonacci(n-1) + fibonacci(n-2));
    }
}
