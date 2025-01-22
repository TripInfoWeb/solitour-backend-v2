package solitour_backend.solitour.media_location.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
import solitour_backend.solitour.media_location.dto.response.MediaMainResponse;
import solitour_backend.solitour.media_location.dto.response.MediaResponse;
import solitour_backend.solitour.media_location.media_type.MediaType;
import solitour_backend.solitour.media_location.service.MediaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    @GetMapping
    public ResponseEntity<Page<MediaResponse>> getMediaByType(@RequestParam("type") MediaType mediaType,
                                                              @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<MediaResponse> mediaList = mediaService.getMediaByType(mediaType, pageable);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/main")
    public ResponseEntity<List<MediaMainResponse>> getMediaMain(@AuthenticationPrincipal Long userId) {
        List<MediaMainResponse> mediaByMain = mediaService.getMediaByMain(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mediaByMain);
    }
}
