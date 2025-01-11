package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.Plan;

import java.util.List;

public record PlanResponse(Long planId,
                           String title,
                           String createdDate,
                           List<UserDaysResponse> days) {
    public static PlanResponse from(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getTitle(),
                plan.getCreatedDate(),
                plan.getDays().stream()
                        .map(UserDaysResponse::from)
                        .toList()
        );
    }
}
