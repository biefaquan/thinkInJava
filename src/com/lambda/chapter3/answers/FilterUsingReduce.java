package com.lambda.chapter3.answers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 没有看明白
 */
public class FilterUsingReduce {
    //只用 reduce 和 Lambda 表达式写出实现 Stream 上的 filter操作的代码，如果不想返回Stream ，可以返回一个 List 。
    public static <I> List<I> filter(Stream<I> stream, Predicate<I> predicate) {
        List<I> initial = new ArrayList<>();
        return stream.reduce(initial, (List<I> acc, I x) -> {
            if (predicate.test(x)) {
                // We are copying data from acc to new list instance. It is very inefficient,
                // but contract of Stream.reduce method requires that accumulator function does
                // not mutate its arguments.
                // Stream.collect method could be used to implement more efficient mutable reduction,
                // but this exercise asks to use reduce method explicitly.
                List<I> newAcc = new ArrayList<>(acc);
                newAcc.add(x);
                return newAcc;
            } else {
                return acc;
            }
        }, FilterUsingReduce::combineLists);
    }

    private static <I> List<I> combineLists(List<I> left, List<I> right) {
        // We are copying left to new list to avoid mutating it.
        List<I> newLeft = new ArrayList<>(left);
        newLeft.addAll(right);
        return newLeft;
    }
}
