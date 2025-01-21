package solitour_backend.solitour.media_location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MediaMainResponse {
    private Long id;
    private String mediaName;
    private String placeName;
    private String mediaImage;
}
