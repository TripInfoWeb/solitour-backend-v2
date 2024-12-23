package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.travel_plan.entity.Plan;

import java.time.LocalDateTime;
import java.util.List;

public record TravelPlanResponse(Long id, String title, String createdDate, List<DaysResponse> days) {
    public static TravelPlanResponse from(Plan plan) {
        List<DaysResponse> daysResponses = DaysResponse.from(plan.getDays());
        return new TravelPlanResponse(plan.getId(), plan.getTitle(), plan.getCreatedDate(), daysResponses);
    }
}
