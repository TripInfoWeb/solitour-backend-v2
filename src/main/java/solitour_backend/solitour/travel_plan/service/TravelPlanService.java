package solitour_backend.solitour.travel_plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.media_location.repository.MediaLocationRepository;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;
import solitour_backend.solitour.tourist_spot.repository.TouristSpotRepository;
import solitour_backend.solitour.travel_plan.dto.TravelPlanListResponse;
import solitour_backend.solitour.travel_plan.dto.TravelRequest;
import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;
import solitour_backend.solitour.travel_plan.entity.Plan;
import solitour_backend.solitour.travel_plan.repository.TravelPlanRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelPlanService {
    private final TouristSpotRepository touristSpotRepository;
    private final MediaLocationRepository mediaLocationRepository;
    private final TravelPlanRepository planRepository;

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

        List<List<Spot>> combinations = generateCombinationsWithPriority(convertedMediaLocations,convertedTouristSpots , spotsPerDay * days, maxResults);

        if (combinations.isEmpty()) {
            throw new RuntimeException("Not enough travel spots to fulfill the plan.");
        }

        int planIndex = 1;
        for (List<Spot> combination : combinations) {
            Plan plan = new Plan();
            plan.setTitle("Plan " + planIndex);
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

//    private Days convertToDays(int dayNumber, List<?> spots, String category) {
//        Days days = new Days();
//        days.setDayNumber(dayNumber);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String json = objectMapper.writeValueAsString(
//                    spots.stream().map(spot -> {
//                        if (spot instanceof TouristSpot) {
//                            return ((TouristSpot) spot).getTitle();
//                        } else if (spot instanceof MediaLocation) {
//                            return ((MediaLocation) spot).getLocationName();
//                        }
//                        return null;
//                    }).toList()
//            );
//            if (category.equals("음식점")) {
//                days.setRestaurants(json);
//            } else {
//                days.setLocations(json);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error converting spots to JSON", e);
//        }
//
//        return days;
//    }

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
}
