package solitour_backend.solitour.media_location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.media_location.media_type.MediaType;

import java.util.List;

public interface MediaLocationRepository extends JpaRepository<MediaLocation, Long>, MediaLocationRepositoryCustom {
    List<MediaLocation> findByMediaNameIn(List<String> placeNames);

    @Query(value = "SELECT * FROM media_location where media_type = :mediaType ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<MediaLocation> findRandomByMediaType(@Param("mediaType") MediaType mediaType);
}
