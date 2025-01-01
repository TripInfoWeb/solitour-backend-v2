package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.Days;

import java.util.List;

public record UserDaysResponse(Long id, int dayNumber, List<DaysDetailResponse> daysDetailResponses) {
    public static UserDaysResponse from(Days days) {
        return new UserDaysResponse(
                days.getId(),
                days.getDayNumber(),
                days.getDetails().stream()
                        .map(DaysDetailResponse::from)
                        .toList()
        );
    }
}
