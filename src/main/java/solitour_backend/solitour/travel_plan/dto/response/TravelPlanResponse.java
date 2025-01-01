package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.Plan;

import java.util.List;

public record TravelPlanResponse(Long id, String title, String createdDate, List<List<DaysDetailResponse>> days) {
    public static TravelPlanResponse from(Plan plan) {
        List<List<DaysDetailResponse>> daysResponses = DaysResponse.from(plan.getDays());
        return new TravelPlanResponse(plan.getId(), plan.getTitle(), plan.getCreatedDate(), daysResponses);
    }
}
