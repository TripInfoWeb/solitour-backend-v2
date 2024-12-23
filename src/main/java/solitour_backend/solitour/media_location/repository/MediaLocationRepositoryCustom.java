package solitour_backend.solitour.media_location.repository;

import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;

import java.util.List;


@NoRepositoryBean
public interface MediaLocationRepositoryCustom {
    List<MediaLocation> findLocationByTitle(List<String> title);
}
