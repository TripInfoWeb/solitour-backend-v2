package solitour_backend.solitour.travel_plan.dto.request;

import java.util.List;

public record PlanRequest(String title, List<DayRequest> days) {
}
