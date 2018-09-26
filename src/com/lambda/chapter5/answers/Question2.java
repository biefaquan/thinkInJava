//package com.lambda.chapter5.answers;
//
//import com.lambda.chapter1.examples.Artist;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Stream;
//
//public class Question2 {
//    Stream<String> names = Stream.of("John Lennon", "Paul McCartney",
//            "George Harrison", "Ringo Starr", "Pete Best", "Stuart Sutcliffe");
//
//    private static Comparator<Artist> byNameLength = Comparator.comparing(artist -> artist.getName().length());
//
//    // a 找出名字最长的艺术家，分别使用收集器和第 3 章介绍过的 reduce 高阶函数实现。
//    public static Artist byReduce(List<Artist> artists) {
//        return artists.stream()
//                .reduce((acc, artist) -> )
//    }
//}
