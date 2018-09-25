package com.lambda.chapter4.answers;

import com.lambda.chapter1.examples.Artist;

import java.util.stream.Stream;

public interface PerformanceFixed {
    public String getName();

    public Stream<Artist> getMusicians();

    // getAllMusicians 方法返回乐队名和乐队成员
    public default Stream<Artist> getAllMusicians() {
        return getMusicians().flatMap(artist -> Stream.concat(
                Stream.of(artist), artist.getMembers()
        ));
    }
}
