package solitour_backend.solitour.travel_plan.util;

import solitour_backend.solitour.travel_plan.entity.DaysDetail;
import solitour_backend.solitour.travel_plan.service.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;


public class SpotShortestPathSorter {

    /**
     * 주어진 Spot 조합 리스트(List<List<Spot>>)를 각 조합 내에서 최단 경로로 정렬합니다.
     *
     * @param combinations Spot 조합 리스트
     * @return 최단 경로로 정렬된 Spot 조합 리스트
     */
    public List<List<Spot>> sortCombinationsByShortestPath(List<List<Spot>> combinations) {
        if (combinations == null || combinations.isEmpty()) {
            return new ArrayList<>();
        }

        // 각 조합(List<Spot>)을 정렬
        return combinations.stream()
                .map(this::sortSpotsByShortestPath) // 각 조합을 정렬
                .collect(Collectors.toList());
    }

    /**
     * 단일 Spot 리스트를 최단 경로로 정렬합니다.
     *
     * @param spots Spot 리스트
     * @return 정렬된 Spot 리스트
     */
    private List<Spot> sortSpotsByShortestPath(List<Spot> spots) {
        if (spots == null || spots.isEmpty()) {
            return new ArrayList<>();
        }

        List<Spot> sortedSpots = new ArrayList<>();
        List<Spot> remainingSpots = new ArrayList<>(spots);

        // 시작점을 첫 번째 Spot으로 설정
        Spot currentSpot = remainingSpots.remove(0);
        sortedSpots.add(currentSpot);

        // 나머지 Spot들에 대해 최단 경로 계산
        while (!remainingSpots.isEmpty()) {
            Spot nextSpot = findNearestSpot(currentSpot, remainingSpots);
            sortedSpots.add(nextSpot);
            remainingSpots.remove(nextSpot);
            currentSpot = nextSpot;
        }

        return sortedSpots;
    }

    /**
     * 현재 Spot에서 가장 가까운 Spot을 찾습니다.
     *
     * @param current 현재 Spot
     * @param candidates 후보 Spot 리스트
     * @return 가장 가까운 Spot
     */
    private Spot findNearestSpot(Spot current, List<Spot> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(candidate -> calculateDistance(
                        current.getLatitude(), current.getLongitude(),
                        candidate.getLatitude(), candidate.getLongitude())))
                .orElseThrow(() -> new RuntimeException("No candidates available"));
    }

    /**
     * 두 위도와 경도를 기반으로 거리 계산.
     *
     * @param lat1 첫 번째 위도
     * @param lon1 첫 번째 경도
     * @param lat2 두 번째 위도
     * @param lon2 두 번째 경도
     * @return 두 지점 간의 거리 (km)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Earth radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }
}