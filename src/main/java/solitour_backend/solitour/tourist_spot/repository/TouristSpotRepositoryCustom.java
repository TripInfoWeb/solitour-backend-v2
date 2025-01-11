package solitour_backend.solitour.tourist_spot.repository;

import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;
import solitour_backend.solitour.tourist_spot.spot_type.SpotType;

import java.util.List;


@NoRepositoryBean
public interface TouristSpotRepositoryCustom {
    List<TouristSpot> findTouristSpotBySpotType(List<SpotType> spotType);
}
