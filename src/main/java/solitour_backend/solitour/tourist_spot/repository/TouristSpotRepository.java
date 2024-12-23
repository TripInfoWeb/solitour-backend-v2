package solitour_backend.solitour.tourist_spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;

import java.util.List;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long>,TouristSpotRepositoryCustom {
}
