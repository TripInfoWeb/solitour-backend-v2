package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.Days;

import java.util.List;

public record DaysResponse(Long id, int dayNumber, String locations) {
    public static List<List<DaysDetailResponse>> from(List<Days> days) {
        return days.stream()
                .map(day-> DaysDetailListResponse.from(day.getDetails()))
                .toList();
    }
}
