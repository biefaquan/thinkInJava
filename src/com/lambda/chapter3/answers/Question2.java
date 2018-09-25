package com.lambda.chapter3.answers;

import com.lambda.chapter1.examples.Artist;

import java.util.List;

/**
 * Created by bfq on 2018/9/25
 */
public class Question2 {
    /*
    修改如下代码，将外部迭代转换成内部迭代：
        int totalMembers = 0;
        for (Artist artist : artists) {
            Stream<Artist> members = artist.getMembers();
            totalMembers += members.count();
        }
     */
    public static int countBandMembersInternal(List<Artist> artists) {
        return artists.stream()
                .map(artist -> artist.getMembers().count())
                .reduce(0L, Long::sum)
                .intValue();
    }
}
