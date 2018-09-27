package com.lambda.chapter6.answers;

import java.util.stream.IntStream;

/**
 * Created by bfq on 2018/9/27
 */
public class Question1 {
    /* 例 6-10 中的代码顺序求流中元素的平方和，将其改为并行处理。
        public static int sequentialSumOfSquares(IntStream range) {
             return range.map(x -> x * x)
                        .sum();
        }
     */
    public static int sequentialSumOfSquares(IntStream range) {
        return range.parallel().map(x -> x * x)
                .sum();
    }
}
