package solitour_backend.solitour.travel_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.auth.config.Authenticated;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
import solitour_backend.solitour.travel_plan.dto.request.TravelRequest;
import solitour_backend.solitour.travel_plan.dto.request.UserPlanRequest;
import solitour_backend.solitour.travel_plan.dto.response.TravelPlanListResponse;
import solitour_backend.solitour.travel_plan.dto.response.UserPlanResponse;
import solitour_backend.solitour.travel_plan.entity.UserPlan;
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

    @GetMapping("/user-plan/all")
    public ResponseEntity<Page<UserPlan>> getPagedLatestUserPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        return ResponseEntity.ok(travelPlanService.getLatestUserPlans(page, size));
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

    @PutMapping("/user-plan/{planId}")
    public ResponseEntity<String> updateUserPlan(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long planId,
            @RequestBody UserPlanRequest userPlanRequest) {

        travelPlanService.updateUserPlan(userId,planId, userPlanRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user-plan/title/{planId}")
    public ResponseEntity<String> updateUserPlanTitle(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long planId,
            @RequestBody String title) {

        travelPlanService.updateUserPlanTitle(userId,planId, title);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user-plan/{planId}")
    public ResponseEntity<String> deleteUserPlan(@AuthenticationPrincipal Long userId, @PathVariable Long planId) {
        travelPlanService.deleteUserPlan(userId, planId);
        return ResponseEntity.noContent().build();
    }
}
