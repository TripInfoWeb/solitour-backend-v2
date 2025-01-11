package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.DaysDetail;

public record DaysDetailResponse(Long id, String placeName, String address, double latitude, double longitude) {
    public static DaysDetailResponse from(DaysDetail daysDetail) {
        return new DaysDetailResponse(daysDetail.getId(), daysDetail.getPlaceName(), daysDetail.getAddress(), daysDetail.getLatitude(), daysDetail.getLongitude());
    }

}
