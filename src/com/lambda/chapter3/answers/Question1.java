package com.lambda.chapter3.answers;

import com.lambda.chapter1.examples.Album;
import com.lambda.chapter1.examples.Artist;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by bfq on 2018/9/25
 */
public class Question1 {
    // a. 编写一个求和函数，计算流中所有数之和。例如， int addUp(Stream<Integer> numbers) ；
    public static int addUp(Stream<Integer> numbers) {
        return numbers.reduce(0, (acc, x) -> acc + x);//此种方法不推荐使用
    }

    // b. 编写一个函数，接受艺术家列表作为参数，返回一个字符串列表，其中包含艺术家的姓名和国籍；
    public static List<String> getNamesAndOrigins(List<Artist> artists) {
        return artists.stream()
                .flatMap(a -> Stream.of(a.getName(), a.getNationality()))
                .collect(Collectors.toList());
    }

    // c. 编写一个函数，接受专辑列表作为参数，返回一个由最多包含 3 首歌曲的专辑组成的列表。
    public static List<Album> getAlbumsWithAtMostThreeTracks(List<Album> input) {
        return input.stream()
                .filter(album -> album.getTrackList().size() <= 3)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(getNamesAndOrigins(Arrays.asList(new Artist("托尔斯泰", "美国"))));
    }
}
