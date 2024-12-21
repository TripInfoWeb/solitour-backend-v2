package solitour_backend.solitour.media.media_type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MediaType {
    DRAMA("드라마"),
    ARTIST("아티스트"),
    MOVIE("영화"),
    ENTERTAINMENT("예능");

    private final String type;

    MediaType(String type) {
        this.type = type;
    }

    public static MediaType fromName(String type) {
        return Arrays.stream(MediaType.values())
                .filter(e -> e.getType().equals(type))
                .findAny()
                .orElse(null);
    }
}
