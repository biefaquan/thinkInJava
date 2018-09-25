package com.lambda.chapter4.answers;

import com.lambda.chapter1.examples.Artist;

import java.util.stream.Stream;

public interface Performance {
    public String getName();

    public Stream<Artist> getMusicians();
}
