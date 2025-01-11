package solitour_backend.solitour.media_location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solitour_backend.solitour.media_location.entity.Media;
import solitour_backend.solitour.media_location.media_type.MediaType;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("SELECT m FROM Media m WHERE m.mediaType = :mediaType")
    Page<Media> findMediaByType(@Param("mediaType") MediaType mediaType, Pageable pageable);
}
