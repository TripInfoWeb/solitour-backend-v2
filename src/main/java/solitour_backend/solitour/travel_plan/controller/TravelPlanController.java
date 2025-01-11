package solitour_backend.solitour.travel_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.auth.config.Authenticated;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
import solitour_backend.solitour.travel_plan.dto.request.TravelRequest;
import solitour_backend.solitour.travel_plan.dto.request.UserPlanRequest;
import solitour_backend.solitour.travel_plan.dto.response.TravelPlanListResponse;
import solitour_backend.solitour.travel_plan.dto.response.UserPlanResponse;
import solitour_backend.solitour.travel_plan.service.TravelPlanService;

import java.util.List;

@Authenticated
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

    @GetMapping("/user-plan")
    public ResponseEntity<List<UserPlanResponse>> getUserPlanList(@AuthenticationPrincipal Long userId) {
        List<UserPlanResponse> userPlan = travelPlanService.getUserPlanList(userId);
        return ResponseEntity.ok(userPlan);
    }


    @PostMapping("/user-plan")
    public ResponseEntity<Void> saveUserPlan(@AuthenticationPrincipal Long userId, @RequestParam Long planId) {
        travelPlanService.saveUserPlan(userId, planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-plan/{userPlanId}")
    public ResponseEntity<UserPlanResponse> getUserPlan(@PathVariable Long userPlanId) {
        UserPlanResponse userPlan = travelPlanService.getUserPlan(userPlanId);
        return ResponseEntity.ok(userPlan);
    }

    @PutMapping("/user-plan/{userPlanId}")
    public ResponseEntity<String> updateUserPlan(
            @PathVariable Long userPlanId,
            @RequestBody UserPlanRequest userPlanRequest) {

        travelPlanService.updateUserPlan(userPlanId, userPlanRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user-plan/{userPlanId}")
    public ResponseEntity<String> deleteUserPlan(@PathVariable Long userPlanId) {
        travelPlanService.deleteUserPlan(userPlanId);
        return ResponseEntity.noContent().build();
    }
}
