package com.lambda.chapter4.answers;

import com.lambda.chapter1.examples.Artist;

import java.util.List;
import java.util.Optional;
/*
重构该类，使得 getArtist 方法返回一个 Optional<Artist> 对象。此外，还需重构 getArtistName 方法，保持相同的行为。
 */
public class ArtistsFixed {
    private List<Artist> artists;
    public ArtistsFixed(List<Artist> artists) {
        this.artists = artists;
    }
    public Optional<Artist> getArtist(int index) {
        if (index < 0 || index >= artists.size()) {
            return Optional.empty();
        }
        return Optional.of(artists.get(index));
    }

    public String getArtistName(int index) {
        Optional<Artist> artist = getArtist(index);
        return artist.map(Artist::getName)
                .orElse("unknown");
    }
}
