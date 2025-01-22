package solitour_backend.solitour.media_location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.media_location.dto.response.MediaMainResponse;
import solitour_backend.solitour.media_location.dto.response.MediaResponse;
import solitour_backend.solitour.media_location.dto.response.MediaTopKeyWordResponse;
import solitour_backend.solitour.media_location.entity.Media;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.media_location.media_type.MediaType;
import solitour_backend.solitour.media_location.repository.MediaLocationRepository;
import solitour_backend.solitour.media_location.repository.MediaRepository;
import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;
import solitour_backend.solitour.travel_plan.entity.Plan;
import solitour_backend.solitour.travel_plan.entity.UserPlan;
import solitour_backend.solitour.travel_plan.repository.DaysDetailRepository;
import solitour_backend.solitour.travel_plan.repository.DaysRepository;
import solitour_backend.solitour.travel_plan.repository.TravelPlanRepository;
import solitour_backend.solitour.travel_plan.repository.UserPlanRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService {
    private final MediaLocationRepository mediaLocationRepository;
    private final MediaRepository mediaRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRepository userPlanRepository;
    private final DaysRepository daysRepository;
    private final DaysDetailRepository daysDetailRepository;

    public Page<MediaResponse> getMediaByType(MediaType mediaType, Pageable pageable) {
        Page<Media> mediaPage = mediaRepository.findMediaByType(mediaType, pageable);
        return mediaPage.map(MediaResponse::from);
    }

    public List<MediaMainResponse> getMediaByMain(Long userId) {
        List<Plan> planList = userPlanRepository.getUserPlanList(userId)
                .stream()
                .map(UserPlan::getPlan)
                .collect(Collectors.toList());


        List<Days> dayList = planList.stream()
                .map(daysRepository::findByPlan).toList();

        List<DaysDetail> daysDetails = daysDetailRepository.findByDaysIn(dayList);

        List<String> placeNames = daysDetails.stream()
                .map(DaysDetail::getPlaceName)
                .collect(Collectors.toList());

        List<MediaLocation> mediaLocations = mediaLocationRepository.findByMediaNameIn(placeNames);

        Map<MediaType, Long> mediaTypeLongMap = mediaLocations.stream().collect(Collectors.groupingBy(MediaLocation::getMediaType, Collectors.counting()));

        MediaType mostCommonMediaType = mediaTypeLongMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(
                        () -> new IllegalArgumentException("No MediaType found"));

        List<MediaLocation> randomMediaLocation = mediaLocationRepository.findRandomByMediaType(mostCommonMediaType);

        List<MediaMainResponse> mediaList = new ArrayList<>();

        for (MediaLocation mediaLocation : randomMediaLocation) {
            Media media = mediaRepository.findByMediaTypeAndMediaName(mostCommonMediaType, mediaLocation.getMediaName());

            mediaList.add(new MediaMainResponse(media.getId(), media.getMediaName(), mediaLocation.getPlaceName(), media.getMediaImage()));
        }

        return mediaList;
    }

    public List<MediaTopKeyWordResponse> getMediaTopKeyWord() {
        List<Plan> planList = userPlanRepository.findAll()
                .stream()
                .map(UserPlan::getPlan)
                .collect(Collectors.toList());

        List<Days> dayList = planList.stream()
                .map(daysRepository::findByPlan).toList();

        List<DaysDetail> daysDetails = daysDetailRepository.findByDaysIn(dayList);

        List<String> placeNames = daysDetails.stream()
                .map(DaysDetail::getPlaceName)
                .collect(Collectors.toList());

        return null;
    }


}
