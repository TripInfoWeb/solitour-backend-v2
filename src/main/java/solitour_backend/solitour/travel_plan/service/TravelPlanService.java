package solitour_backend.solitour.travel_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.media_location.repository.MediaLocationRepository;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;
import solitour_backend.solitour.tourist_spot.repository.TouristSpotRepository;
import solitour_backend.solitour.travel_plan.dto.request.UserPlanRequest;
import solitour_backend.solitour.travel_plan.dto.response.TravelPlanListResponse;
import solitour_backend.solitour.travel_plan.dto.request.TravelRequest;
import solitour_backend.solitour.travel_plan.dto.response.UserPlanResponse;
import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;
import solitour_backend.solitour.travel_plan.entity.Plan;
import solitour_backend.solitour.travel_plan.entity.UserPlan;
import solitour_backend.solitour.travel_plan.repository.DaysDetailRepository;
import solitour_backend.solitour.travel_plan.repository.TravelPlanRepository;
import solitour_backend.solitour.travel_plan.repository.UserPlanRepository;
import solitour_backend.solitour.travel_plan.util.SpotShortestPathSorter;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TravelPlanService {
    private final TouristSpotRepository touristSpotRepository;
    private final MediaLocationRepository mediaLocationRepository;
    private final TravelPlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final UserRepository userRepository;
    private final DaysDetailRepository daysDetailRepository;

    @Transactional
    public TravelPlanListResponse calculateTravelPlan(TravelRequest request) {
        List<Plan> plans = new ArrayList<>();
        int spotsPerDay = 6;
        int days = request.days();
        int maxResults = 3;

        List<TouristSpot> filteredTouristSpots = touristSpotRepository.findTouristSpotBySpotType(request.preferredTrips());
        List<MediaLocation> filteredMediaLocations = mediaLocationRepository.findLocationByTitle(request.contentTitles());

        List<Spot> convertedTouristSpots = new ArrayList<>(filteredTouristSpots);
        List<Spot> convertedMediaLocations = new ArrayList<>(filteredMediaLocations);
        SpotShortestPathSorter sorter = new SpotShortestPathSorter();

        List<List<Spot>> combinations = generateCombinationsWithPriority(convertedMediaLocations,convertedTouristSpots , spotsPerDay * days, maxResults);
        List<List<Spot>> sortedCombinations = sorter.sortCombinationsByShortestPath(combinations);

        if (combinations.isEmpty()) {
            throw new RuntimeException("Not enough travel spots to fulfill the plan.");
        }

        int planIndex = 1;
        for (List<Spot> combination : sortedCombinations) {
            Plan plan = new Plan();
            plan.setTitle("Plan " + planIndex);
            plan.setContentCategory(request.contentCategory());
            plan.setCreatedDate(java.time.LocalDate.now().toString());

            List<Days> daysList = createDaysForPlan(combination, days, spotsPerDay);
            for (Days day : daysList) {
                plan.addDay(day);
            }

            plans.add(plan);
            planIndex++;
        }

        List<Plan> travelPlans = planRepository.saveAll(plans);

        return TravelPlanListResponse.from(travelPlans);
    }

    private List<Days> createDaysForPlan(List<Spot> combination, int days, int spotsPerDay) {
        List<Days> daysList = new ArrayList<>();
        int spotIndex = 0;

        for (int dayNumber = 1; dayNumber <= days; dayNumber++) {
            Days day = new Days();
            day.setDayNumber(dayNumber);

            // 하루에 해당하는 DaysDetail 생성
            List<DaysDetail> dayDetails = new ArrayList<>();
            for (int i = 0; i < spotsPerDay; i++) {
                if (spotIndex >= combination.size()) break;

                Spot spot = combination.get(spotIndex);
                DaysDetail detail = DaysDetail.builder()
                        .placeName(spot.getPlaceName())
                        .address(spot.getAddress())
                        .latitude(spot.getLatitude())
                        .longitude(spot.getLongitude())
                        .day(day)
                        .build();

                dayDetails.add(detail);
                spotIndex++;
            }

            day.setDetails(dayDetails);
            daysList.add(day);
        }

        return daysList;
    }

    private List<List<Spot>> generateCombinationsWithPriority(
            List<Spot> priorityItems,
            List<Spot> otherItems,
            int size,
            int maxResults
    ) {
        List<List<Spot>> combinations = new ArrayList<>();

        generateCombinationsHelper(priorityItems, new ArrayList<>(), 0, size, combinations, maxResults);

        if (combinations.size() < maxResults) {
            List<Spot> remainingItems = new ArrayList<>(otherItems);
            remainingItems.removeAll(priorityItems);

            generateCombinationsHelper(remainingItems, new ArrayList<>(), 0, size, combinations, maxResults);
        }

        return combinations;
    }

    private void generateCombinationsHelper(
            List<Spot> items,
            List<Spot> tempCombination,
            int start,
            int size,
            List<List<Spot>> combinations,
            int maxResults
    ) {
        if (combinations.size() >= maxResults) {
            return;
        }

        if (tempCombination.size() == size) {
            combinations.add(new ArrayList<>(tempCombination));
            return;
        }

        for (int i = start; i < items.size(); i++) {
            tempCombination.add(items.get(i));
            generateCombinationsHelper(items, tempCombination, i + 1, size, combinations, maxResults);
            tempCombination.remove(tempCombination.size() - 1);
        }
    }

    @Transactional
    public void saveUserPlan(Long userId, Long planId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID: " + planId));

        UserPlan userPlan = UserPlan.builder()
                .user(user)
                .plan(plan)
                .build();

        userPlanRepository.save(userPlan);
    }

    public UserPlanResponse getUserPlan(Long userPlanId) {
        UserPlan userPlans = userPlanRepository.getUserPlan(userPlanId);

        return UserPlanResponse.from(userPlans);
    }

    public List<UserPlanResponse> getUserPlanList(Long userId) {
        List<UserPlan> userPlans = userPlanRepository.getUserPlanList(userId);
        if (userPlans.isEmpty()) {
            throw new IllegalArgumentException("No UserPlans found for user ID: " + userId);
        }
        return userPlans.stream()
                .map(UserPlanResponse::from)
                .toList();
    }

    @Transactional
    public void updateUserPlan(Long userPlanId, UserPlanRequest userPlanRequest) {
        UserPlan userPlan = userPlanRepository.findById(userPlanId)
                .orElseThrow(() -> new IllegalArgumentException("UserPlan not found with ID: " + userPlanId));

        Plan plan = userPlan.getPlan();
        plan.setTitle(userPlanRequest.plan().title());

        List<Days> updatedDaysList = userPlanRequest.plan().days().stream()
                .map(dayRequest -> {
                    Days day = new Days();
                    day.setDayNumber(dayRequest.dayNumber());
                    day.setPlan(plan);

                    List<DaysDetail> updatedDetails = dayRequest.details().stream()
                            .map(detailRequest -> DaysDetail.builder()
                                    .placeName(detailRequest.placeName())
                                    .address(detailRequest.address())
                                    .latitude(detailRequest.latitude())
                                    .longitude(detailRequest.longitude())
                                    .day(day)
                                    .build())
                            .toList();

                    day.setDetails(updatedDetails);
                    return day;
                })
                .toList();

        plan.getDays().clear();
        plan.getDays().addAll(updatedDaysList);

        planRepository.save(plan);
    }

    @Transactional
    public void deleteUserPlan(Long userPlanId) {
        if (!userPlanRepository.existsById(userPlanId)) {
            throw new IllegalArgumentException("UserPlan not found with ID: " + userPlanId);
        }
        userPlanRepository.deleteById(userPlanId);
    }
}
