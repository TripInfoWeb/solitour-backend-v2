package solitour_backend.solitour.travel_plan.dto.request;

import solitour_backend.solitour.tourist_spot.spot_type.SpotType;

import java.util.List;

public record TravelRequest(List<String> contentTitles, String contentCategory, int days, List<SpotType> preferredTrips) {
}
