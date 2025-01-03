package solitour_backend.solitour.media_location.dto.response;

import solitour_backend.solitour.media_location.entity.Media;

public record MediaResponse(Long id, String mediaName, String mediaImage) {

    public static MediaResponse from(Media media) {
        return new MediaResponse(
                media.getId(),
                media.getMediaName(),
                media.getMediaImage()
        );
    }
}