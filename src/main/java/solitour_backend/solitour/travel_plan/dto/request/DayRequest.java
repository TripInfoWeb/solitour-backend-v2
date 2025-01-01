package solitour_backend.solitour.travel_plan.dto.request;

import java.util.List;

public record DayRequest(int dayNumber, List<DaysDetailRequest> details) {
}
