package solitour_backend.solitour.travel_plan.dto;

import solitour_backend.solitour.tourist_spot.spot_type.SpotType;

import java.util.List;

public record TravelRequest(List<String> contentTitles, int days, List<SpotType> preferredTrips) {
}
