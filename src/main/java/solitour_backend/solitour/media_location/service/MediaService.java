package solitour_backend.solitour.media_location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import solitour_backend.solitour.media_location.dto.response.MediaResponse;
import solitour_backend.solitour.media_location.entity.Media;
import solitour_backend.solitour.media_location.media_type.MediaType;
import solitour_backend.solitour.media_location.repository.MediaRepository;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;

    public Page<MediaResponse> getMediaByType(MediaType mediaType, Pageable pageable) {
        Page<Media> mediaPage = mediaRepository.findMediaByType(mediaType, pageable);
        return mediaPage.map(MediaResponse::from);
    }
}
