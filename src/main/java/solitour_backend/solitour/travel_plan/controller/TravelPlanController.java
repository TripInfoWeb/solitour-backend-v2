package solitour_backend.solitour.travel_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.travel_plan.dto.TravelPlanListResponse;
import solitour_backend.solitour.travel_plan.dto.TravelRequest;
import solitour_backend.solitour.travel_plan.service.TravelPlanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel")
public class TravelPlanController {
    private final TravelPlanService travelPlanService;

    @PostMapping("/plan")
    public ResponseEntity<TravelPlanListResponse> createTravelPlan(@RequestBody TravelRequest request) {
        TravelPlanListResponse result = travelPlanService.calculateTravelPlan(request);
        return ResponseEntity.ok(result);
    }
}
