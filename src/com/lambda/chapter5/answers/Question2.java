package com.lambda.chapter5.answers;

import com.lambda.chapter1.examples.Artist;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Question2 {
    Stream<String> names = Stream.of("John Lennon", "Paul McCartney",
            "George Harrison", "Ringo Starr", "Pete Best", "Stuart Sutcliffe");

    private static Comparator<Artist> byNameLength = Comparator.comparing(artist -> artist.getName().length());

    // a 找出名字最长的艺术家，分别使用收集器和第 3 章介绍过的 reduce 高阶函数实现。
    public static Artist byReduce(List<Artist> artists) {
        return artists.stream()
                .reduce((acc, artist) -> {
                    return byNameLength.compare(acc, artist)>=0 ? acc : artist;
                })
                .orElseThrow(RuntimeException::new);
    }

    //收集器
    public static Artist byCollecting(List<Artist> artists) {
        return artists.stream()
                .collect(Collectors.maxBy(byNameLength))
                .orElseThrow(RuntimeException::new);
    }


    // b. 假设一个元素为单词的流，计算每个单词出现的次数。
    public static Map<String, Long> countWords(Stream<String> names) {
        return names.collect(Collectors.groupingBy(name -> name, Collectors.counting()));
    }
}

//todo 基本没有理解其具体的操作？？
//c. 用一个定制的收集器实现 Collectors.groupingBy 方法，不需要提供一个下游收集器，只需实现一个最简单的即可。
class GroupingBy<T, K> implements Collector<T, Map<K, List<T>>, Map<K, List<T>>> {

    private final static Set<Characteristics> characteristics = new HashSet<>();

    static {
        characteristics.add(Characteristics.IDENTITY_FINISH);
    }

    private final Function<? super T, ? extends K> classifier;

    public GroupingBy(Function<? super T, ? extends K> classifier) {
        this.classifier = classifier;
    }

    @Override
    public Supplier<Map<K, List<T>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<K, List<T>>, T> accumulator() {
        return (map, element) -> {
            K key = classifier.apply(element);
            List<T> elements = map.computeIfAbsent(key, k -> new ArrayList<>());
            elements.add(element);
        };
    }

    @Override
    public BinaryOperator<Map<K, List<T>>> combiner() {
        return (left, right) -> {
            right.forEach((key, value) -> {
                left.merge(key, value, (leftValue, rightValue) -> {
                    leftValue.addAll(rightValue);
                    return leftValue;
                });
            });
            return left;
        };
    }

    @Override
    public Function<Map<K, List<T>>, Map<K, List<T>>> finisher() {
        return map -> map;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return characteristics;
    }
}
