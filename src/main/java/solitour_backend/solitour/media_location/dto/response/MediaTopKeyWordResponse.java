package solitour_backend.solitour.media_location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MediaTopKeyWordResponse {
    private String mediaName;
    private List<String> placeNameList;
}
