package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;

import java.util.List;

public record DaysDetailListResponse() {

    public static  List<DaysDetailResponse> from(List<DaysDetail> daysDetail) {
        return daysDetail.stream()
                .map(DaysDetailResponse::from)
                .toList();
    }
}
