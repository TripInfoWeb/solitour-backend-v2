package solitour_backend.solitour.tourist_spot.spot_type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SpotType {
    CULTURAL_FACILITY("문화시설"),
    NOVELTY_EXPERIENCE("이색체험"),
    NATURAL_PLACE("자연관광지"),
    HISTORICAL_PLACE("역사관광지"),
    MARKET("시장");

    private final String type;

    SpotType(String type) {
        this.type = type;
    }

    public static SpotType fromName(String type) {
        return Arrays.stream(SpotType.values())
                .filter(e -> e.getType().equals(type))
                .findAny()
                .orElse(null);
    }
}
