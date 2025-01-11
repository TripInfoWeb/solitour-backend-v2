package solitour_backend.solitour.travel_plan.dto.response;

import solitour_backend.solitour.travel_plan.entity.UserPlan;

public record UserPlanResponse(PlanResponse plan) {
    public static UserPlanResponse from(UserPlan userPlan) {
        return new UserPlanResponse(
                PlanResponse.from(userPlan.getPlan())
        );
    }

}
