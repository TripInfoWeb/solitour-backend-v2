package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.travel_plan.entity.Days;

import java.util.List;

public record DaysResponse(Long id, int dayNumber, String locations) {
    public static List<DaysResponse> from(List<Days> days) {
        return days.stream()
                .map(day -> new DaysResponse(
                        day.getId(),
                        day.getDayNumber(),
                        day.getLocations()
                ))
                .toList();
    }
}
