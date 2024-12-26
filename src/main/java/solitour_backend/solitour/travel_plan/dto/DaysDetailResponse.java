package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;

public record DaysDetailResponse(Long id, String placeName, double latitude, double longitude) {
    public static DaysDetailResponse from(DaysDetail daysDetail) {
        return new DaysDetailResponse(daysDetail.getId(), daysDetail.getPlaceName(), daysDetail.getLatitude(), daysDetail.getLongitude());
    }
}
