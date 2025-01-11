package solitour_backend.solitour.media_location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;
import solitour_backend.solitour.tourist_spot.repository.TouristSpotRepositoryCustom;

import java.util.List;

public interface MediaLocationRepository extends JpaRepository<MediaLocation, Long>, MediaLocationRepositoryCustom {
}
