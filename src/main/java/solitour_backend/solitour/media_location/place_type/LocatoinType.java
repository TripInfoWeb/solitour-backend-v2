package solitour_backend.solitour.media_location.place_type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LocatoinType {
    STORE("상점"),
    PLAYGROUND("장소"),
    STAY("숙소"),
    CAFE("카페"),
    RESTAURANT("식당");

    private final String type;

    LocatoinType(String type) {
        this.type = type;
    }

    public static LocatoinType fromName(String type) {
        return Arrays.stream(LocatoinType.values())
                .filter(e -> e.getType().equals(type))
                .findAny()
                .orElse(null);
    }
}
