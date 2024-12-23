package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.travel_plan.entity.Plan;

import java.util.List;

public record TravelPlanListResponse(List<TravelPlanResponse> plans) {
    public static TravelPlanListResponse from(List<Plan> plans) {
        List<TravelPlanResponse> responses = plans.stream()
                .map(TravelPlanResponse::from)
                .toList();
        return new TravelPlanListResponse(responses);
    }
}
