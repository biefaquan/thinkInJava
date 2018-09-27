package com.lambda.chapter6.answers;

import java.util.List;

/**
 * Created by bfq on 2018/9/27
 */
public class Question2 {
    /* 例 6-11 中的代码把列表中的数字相乘，然后再将所得结果乘以 5。顺序执行这段程序没有问题，但并行执行时有一个缺陷，使用流并行化执行该段代码，并修复缺陷。
        public static int multiplyThrough(List<Integer> linkedListOfNumbers) {
            return linkedListOfNumbers.stream()
                .reduce(5, (acc, x) -> x * acc);
        }
     */
    //并行化时需要指定一个恒定值。
    public static int multiplyThrough(List<Integer> linkedListOfNumbers) {
        return linkedListOfNumbers.parallelStream()
                .reduce(1, (acc, x) -> x * acc);
    }
}
