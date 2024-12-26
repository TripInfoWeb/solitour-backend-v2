package solitour_backend.solitour.travel_plan.service;

import solitour_backend.solitour.tourist_spot.spot_type.SpotType;

public interface Spot {
    String getPlaceName();

    String getAddress();

    Double getLatitude();

    Double getLongitude();
}
