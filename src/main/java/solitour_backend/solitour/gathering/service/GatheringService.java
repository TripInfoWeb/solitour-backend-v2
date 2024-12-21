package solitour_backend.solitour.gathering.service;

import static solitour_backend.solitour.gathering.repository.GatheringRepositoryCustom.LIKE_COUNT_SORT;
import static solitour_backend.solitour.gathering.repository.GatheringRepositoryCustom.VIEW_COUNT_SORT;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.error.exception.RequestValidationFailedException;
import solitour_backend.solitour.gathering.dto.mapper.GatheringMapper;
import solitour_backend.solitour.gathering.dto.request.GatheringModifyRequest;
import solitour_backend.solitour.gathering.dto.request.GatheringPageRequest;
import solitour_backend.solitour.gathering.dto.request.GatheringRegisterRequest;
import solitour_backend.solitour.gathering.dto.response.GatheringBriefResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringDetailResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringRankResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringResponse;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.gathering.exception.GatheringCategoryNotExistsException;
import solitour_backend.solitour.gathering.exception.GatheringDeleteException;
import solitour_backend.solitour.gathering.exception.GatheringFinishConflictException;
import solitour_backend.solitour.gathering.exception.GatheringNotExistsException;
import solitour_backend.solitour.gathering.repository.GatheringRepository;
import solitour_backend.solitour.gathering_applicants.dto.mapper.GatheringApplicantsMapper;
import solitour_backend.solitour.gathering_applicants.dto.response.GatheringApplicantsResponse;
import solitour_backend.solitour.gathering_applicants.entity.GatheringApplicants;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;
import solitour_backend.solitour.gathering_applicants.exception.GatheringNotManagerException;
import solitour_backend.solitour.gathering_applicants.repository.GatheringApplicantsRepository;
import solitour_backend.solitour.gathering_category.dto.mapper.GatheringCategoryMapper;
import solitour_backend.solitour.gathering_category.dto.response.GatheringCategoryResponse;
import solitour_backend.solitour.gathering_category.entity.GatheringCategory;
import solitour_backend.solitour.gathering_category.repository.GatheringCategoryRepository;
import solitour_backend.solitour.gathering_tag.entity.GatheringTag;
import solitour_backend.solitour.gathering_tag.repository.GatheringTagRepository;
import solitour_backend.solitour.great_gathering.repository.GreatGatheringRepository;
import solitour_backend.solitour.place.dto.mapper.PlaceMapper;
import solitour_backend.solitour.place.dto.request.PlaceModifyRequest;
import solitour_backend.solitour.place.dto.request.PlaceRegisterRequest;
import solitour_backend.solitour.place.dto.response.PlaceResponse;
import solitour_backend.solitour.place.entity.Place;
import solitour_backend.solitour.place.repository.PlaceRepository;
import solitour_backend.solitour.tag.dto.mapper.TagMapper;
import solitour_backend.solitour.tag.dto.response.TagResponse;
import solitour_backend.solitour.tag.entity.Tag;
import solitour_backend.solitour.tag.repository.TagRepository;
import solitour_backend.solitour.user.dto.UserPostingResponse;
import solitour_backend.solitour.user.dto.mapper.UserMapper;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.exception.UserNotExistsException;
import solitour_backend.solitour.user.repository.UserRepository;
import solitour_backend.solitour.user_image.entity.UserImage;
import solitour_backend.solitour.user_image.entity.UserImageRepository;
import solitour_backend.solitour.util.HmacUtils;
import solitour_backend.solitour.zone_category.dto.mapper.ZoneCategoryMapper;
import solitour_backend.solitour.zone_category.dto.response.ZoneCategoryResponse;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.exception.ZoneCategoryNotExistsException;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringService {
    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final ZoneCategoryRepository zoneCategoryRepository;
    private final PlaceRepository placeRepository;
    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;
    private final GatheringTagRepository gatheringTagRepository;
    private final GatheringMapper gatheringMapper;
    private final UserMapper userMapper;
    private final PlaceMapper placeMapper;
    private final ZoneCategoryMapper zoneCategoryMapper;
    private final GreatGatheringRepository greatGatheringRepository;
    private final GatheringApplicantsRepository gatheringApplicantsRepository;
    private final GatheringApplicantsMapper gatheringApplicantsMapper;
    private final GatheringCategoryMapper gatheringCategoryMapper;
    private final UserImageRepository userImageRepository;


    @Transactional
    public GatheringDetailResponse getGatheringDetail(Long userId, Long gatheringId, HttpServletRequest request, HttpServletResponse response) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            throw new GatheringDeleteException("해당하는 모임은 삭제가 되었습니다");
        }

        UserPostingResponse userPostingResponse = userMapper.mapToUserPostingResponse(gathering.getUser());

        List<GatheringTag> gatheringTags = gatheringTagRepository.findAllByGathering_Id(gathering.getId());

        List<TagResponse> tagResponses = new ArrayList<>();

        if (!gatheringTags.isEmpty()) {
            tagResponses = gatheringTags.stream()
                    .map(data ->
                            tagMapper.mapToTagResponse(data.getTag()))
                    .toList();
        }
        GatheringCategory gatheringCategory = gathering.getGatheringCategory();

        GatheringCategoryResponse gatheringCategoryResponse = gatheringCategoryMapper.mapToCategoryResponse(
                gatheringCategory);

        PlaceResponse placeResponse = placeMapper.mapToPlaceResponse(gathering.getPlace());

        ZoneCategoryResponse zoneCategoryResponse = zoneCategoryMapper.mapToZoneCategoryResponse(
                gathering.getZoneCategory());

        int likeCount = greatGatheringRepository.countByGatheringId(gathering.getId());

        List<GatheringApplicantsResponse> gatheringApplicantsResponses = null;

        User user = gathering.getUser();

        String userImageUrl = userImageRepository.findById(user.getUserImage().getId())
                .map(UserImage::getAddress)
                .orElseGet(
                        () -> userRepository.getProfileUrl(user.getSex())
                );

        GatheringStatus gatheringStatus = null;
        GatheringApplicants gatheringApplicants = gatheringApplicantsRepository.findByGatheringIdAndUserId(gathering.getId(), userId).orElse(null);

        if (Objects.nonNull(gatheringApplicants)) {
            gatheringStatus = gatheringApplicants.getGatheringStatus();
        }


        if (gathering.getUser().getId().equals(userId)) {
            gatheringApplicantsResponses = gatheringApplicantsMapper.mapToGatheringApplicantsResponses(
                    gatheringApplicantsRepository.findAllByGathering_IdAndUserIdNot(gathering.getId(), gathering.getUser().getId()));
        }

        int nowPersonCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(gathering.getId(), GatheringStatus.CONSENT);

        boolean isLike = greatGatheringRepository.existsByGatheringIdAndUserId(gathering.getId(), userId);

        List<GatheringBriefResponse> gatheringRecommend = gatheringRepository.getGatheringRecommend(gathering.getId(),
                gathering.getGatheringCategory().getId(), userId);

        try {
            updateViewCount(gathering, request, response, userId);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }


        return new GatheringDetailResponse(
                gathering.getTitle(),
                gathering.getContent(),
                gathering.getPersonCount(),
                gathering.getViewCount(),
                gathering.getCreatedAt(),
                gathering.getScheduleStartDate(),
                gathering.getScheduleEndDate(),
                gathering.getDeadline(),
                gathering.getIsFinish(),
                gathering.getAllowedSex(),
                gathering.getStartAge(),
                gathering.getEndAge(),
                tagResponses,
                userPostingResponse,
                placeResponse,
                zoneCategoryResponse,
                gatheringCategoryResponse,
                likeCount,
                nowPersonCount,
                isLike,
                gathering.getOpenChattingUrl(),
                userImageUrl,
                gatheringApplicantsResponses,
                gatheringRecommend,
                gatheringStatus
        );
    }


    @Transactional
    public GatheringResponse registerGathering(Long userId, GatheringRegisterRequest gatheringRegisterRequest) {
        PlaceRegisterRequest placeRegisterRequest = gatheringRegisterRequest.getPlaceRegisterRequest();
        Place place = new Place(
                placeRegisterRequest.getSearchId(),
                placeRegisterRequest.getName(),
                placeRegisterRequest.getXAxis(),
                placeRegisterRequest.getYAxis(),
                placeRegisterRequest.getAddress());

        Place savePlace = placeRepository.save(place);
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id 의 User 가 없습니다"));
        GatheringCategory gatheringCategory = gatheringCategoryRepository.findById(
                        gatheringRegisterRequest.getGatheringCategoryId())
                .orElseThrow(
                        () -> new GatheringCategoryNotExistsException("해당하는 id의 category 가 없습니다"));

        ZoneCategory parentZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(null,
                        gatheringRegisterRequest.getZoneCategoryNameParent())
                .orElseThrow(
                        () ->
                                new ZoneCategoryNotExistsException("해당하는 name 의 ZoneCategory 없습니다"));

        ZoneCategory childZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(
                        parentZoneCategory.getId(), gatheringRegisterRequest.getZoneCategoryNameChild())
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("해당하는 name 의 ZoneCategory 없습니다"));

        Gathering gathering =
                new Gathering(
                        user,
                        childZoneCategory,
                        gatheringCategory,
                        savePlace,
                        gatheringRegisterRequest.getTitle(),
                        gatheringRegisterRequest.getContent(),
                        gatheringRegisterRequest.getPersonCount(),
                        0,
                        gatheringRegisterRequest.getScheduleStartDate(),
                        gatheringRegisterRequest.getScheduleEndDate(),
                        false,
                        gatheringRegisterRequest.getDeadline(),
                        gatheringRegisterRequest.getAllowedSex(),
                        gatheringRegisterRequest.getStartAge(),
                        gatheringRegisterRequest.getEndAge(),
                        gatheringRegisterRequest.getOpenChattingUrl()
                );
        Gathering saveGathering = gatheringRepository.save(gathering);

        List<Tag> tags = tagMapper.mapToTags(gatheringRegisterRequest.getTagRegisterRequests());
        List<Tag> saveTags = tagRepository.saveAll(tags);

        GatheringApplicants gatheringApplicants = new GatheringApplicants(gathering, user, GatheringStatus.CONSENT);
        gatheringApplicantsRepository.save(gatheringApplicants);

        for (Tag tag : saveTags) {
            gatheringTagRepository.save(new GatheringTag(tag, saveGathering));
        }
        return gatheringMapper.mapToGatheringResponse(saveGathering);
    }

    @Transactional
    public GatheringResponse modifyGathering(Long userId, Long gatheringId,
                                             GatheringModifyRequest gatheringModifyRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            throw new GatheringDeleteException("해당하는 모임은 삭제가 되었습니다");
        }

        if (!Objects.equals(user, gathering.getUser())) {
            throw new GatheringNotManagerException("해당 유저는 권한이 없습니다");
        }

        GatheringCategory gatheringCategory = gatheringCategoryRepository.findById(
                        gatheringModifyRequest.getGatheringCategoryId())
                .orElseThrow(
                        () -> new GatheringCategoryNotExistsException("모임 카테고리가 존재 하지 않습니다")
                );
        ZoneCategory parentZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(null,
                        gatheringModifyRequest.getZoneCategoryNameParent())
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("부모 지역 카테고리가 존재 하지 않습니다")
                );
        ZoneCategory childZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(
                        parentZoneCategory.getId(), gatheringModifyRequest.getZoneCategoryNameChild())
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("자식 지역 카테고리가 존재 하지 않습니다")
                );

        Place place = gathering.getPlace();
        PlaceModifyRequest placeModifyRequest = gatheringModifyRequest.getPlaceModifyRequest();
        place.setSearchId(placeModifyRequest.getSearchId());
        place.setName(placeModifyRequest.getName());
        place.setXaxis(placeModifyRequest.getXAxis());
        place.setYaxis(placeModifyRequest.getYAxis());
        place.setAddress(placeModifyRequest.getAddress());

        gathering.setTitle(gatheringModifyRequest.getTitle());
        gathering.setContent(gatheringModifyRequest.getContent());

        gathering.setPersonCount(gatheringModifyRequest.getPersonCount());
        gathering.setScheduleStartDate(gatheringModifyRequest.getScheduleStartDate());
        gathering.setScheduleEndDate(gatheringModifyRequest.getScheduleEndDate());
        gathering.setDeadline(gatheringModifyRequest.getDeadline());
        gathering.setAllowedSex(gatheringModifyRequest.getAllowedSex());
        gathering.setStartAge(gatheringModifyRequest.getStartAge());
        gathering.setEndAge(gatheringModifyRequest.getEndAge());
        gathering.setGatheringCategory(gatheringCategory);
        gathering.setZoneCategory(childZoneCategory);
        gathering.setOpenChattingUrl(gatheringModifyRequest.getOpenChattingUrl());

        List<GatheringTag> gatheringTags = gatheringTagRepository.findAllByGathering_Id(gathering.getId());

        gatheringTagRepository.deleteAllByGathering_Id(gathering.getId());

        for (GatheringTag gatheringTag : gatheringTags) {
            tagRepository.deleteById(gatheringTag.getTag().getTagId());
        }

        List<Tag> saveTags = tagRepository.saveAll(
                tagMapper.mapToTags(gatheringModifyRequest.getTagRegisterRequests()));

        for (Tag tag : saveTags) {
            gatheringTagRepository.save(new GatheringTag(tag, gathering));
        }

        return gatheringMapper.mapToGatheringResponse(gathering);
    }

    @Transactional
    public void deleteGathering(Long gatheringId, Long userId) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));

        if (!Objects.equals(user, gathering.getUser())) {
            throw new GatheringNotManagerException("해당 유저는 권한이 없습니다");
        }

        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            return;
        }

        gathering.setIsDeleted(true);

    }

    public Page<GatheringBriefResponse> getPageGathering(Pageable pageable, GatheringPageRequest gatheringPageRequest,
                                                         Long userId) {
        validateGatheringPageRequest(gatheringPageRequest);

        return gatheringRepository.getGatheringPageFilterAndOrder(pageable, gatheringPageRequest, userId);
    }

    public Page<GatheringBriefResponse> getPageGatheringByTag(Pageable pageable, Long userId,
                                                              GatheringPageRequest gatheringPageRequest,
                                                              String decodedTag) {
        validateGatheringPageRequest(gatheringPageRequest);

        return gatheringRepository.getPageGatheringByTag(pageable, gatheringPageRequest, userId, decodedTag);
    }

    public List<GatheringRankResponse> getGatheringRankOrderByLikes() {
        return gatheringRepository.getGatheringRankList();
    }

    public List<GatheringBriefResponse> getGatheringOrderByLikesFilterByCreate3After(Long userId) {
        return gatheringRepository.getGatheringLikeCountFromCreatedIn3(userId);
    }

    @Transactional
    public void setGatheringFinish(Long userId, Long gatheringId) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));

        if (!Objects.equals(user, gathering.getUser())) {
            throw new GatheringNotManagerException("해당 유저는 권한이 없습니다");
        }

        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            throw new GatheringDeleteException("해당 하는 모임은 삭제된 모임입니다");
        }

        if (Boolean.TRUE.equals(gathering.getIsFinish())) {
            throw new GatheringFinishConflictException("이미 모임이 finish 상태입니다");
        }

        gathering.setIsFinish(true);
    }

    @Transactional
    public void setGatheringNotFinish(Long userId, Long gatheringId) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));

        if (!Objects.equals(user, gathering.getUser())) {
            throw new GatheringNotManagerException("해당 유저는 권한이 없습니다");
        }

        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            throw new GatheringDeleteException("해당 하는 모임은 삭제된 모임입니다");
        }

        if (Boolean.FALSE.equals(gathering.getIsFinish())) {
            throw new GatheringFinishConflictException("이미 모임이 not finish 상태입니다");
        }

        gathering.setIsFinish(false);
    }


    private void validateGatheringPageRequest(GatheringPageRequest gatheringPageRequest) {
        // Category 검증
        if (Objects.nonNull(gatheringPageRequest.getCategory())) {
            if (!gatheringCategoryRepository.existsById(gatheringPageRequest.getCategory())) {
                throw new GatheringCategoryNotExistsException("해당하는 모임 카테고리가 없습니다.");
            }
        }

        // Location 검증
        if (Objects.nonNull(gatheringPageRequest.getLocation())) {
            if (!zoneCategoryRepository.existsById(gatheringPageRequest.getLocation())) {
                throw new ZoneCategoryNotExistsException("해당하는 지역 카테고리가 없습니다.");
            }
        }

        // 나이 범위 검증
        if (Objects.nonNull(gatheringPageRequest.getStartAge()) && Objects.nonNull(gatheringPageRequest.getEndAge())) {
            if (gatheringPageRequest.getStartAge() > gatheringPageRequest.getEndAge()) {
                throw new RequestValidationFailedException("시작 나이가 끝 나이보다 클 수 없습니다.");
            }
        } else if (Objects.nonNull(gatheringPageRequest.getStartAge()) || Objects.nonNull(
                gatheringPageRequest.getEndAge())) {
            throw new RequestValidationFailedException("시작 나이와 끝 나이는 둘 다 입력되거나 둘 다 비어 있어야 합니다.");
        }

        // 정렬 방식 검증
        if (Objects.nonNull(gatheringPageRequest.getSort())) {
            if (!LIKE_COUNT_SORT.equals(gatheringPageRequest.getSort()) && !VIEW_COUNT_SORT.equals(gatheringPageRequest.getSort())) {
                throw new RequestValidationFailedException("잘못된 정렬 코드입니다.");
            }
        }

        // 날짜 검증
        if (Objects.nonNull(gatheringPageRequest.getStartDate()) && Objects.nonNull(
                gatheringPageRequest.getEndDate())) {

            if (gatheringPageRequest.getStartDate().isAfter(gatheringPageRequest.getEndDate())) {
                throw new RequestValidationFailedException("시작 날짜가 종료 날짜보다 나중일 수 없습니다.");
            }
        } else if (Objects.nonNull(gatheringPageRequest.getStartDate()) || Objects.nonNull(
                gatheringPageRequest.getEndDate())) {
            throw new RequestValidationFailedException("시작 날짜와 종료 날짜는 둘 다 입력되거나 둘 다 비어 있어야 합니다.");
        }
    }


    public void updateViewCount(Gathering gathering, HttpServletRequest request, HttpServletResponse response, Long userId) throws Exception {
        String cookieName = "viewed_gatherings";
        Cookie[] cookies = request.getCookies();
        Cookie postCookie = null;

        if (Objects.nonNull(cookies)) {
            postCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
        }

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cookieData = userId + "_" + gathering.getId() + "_" + now.format(formatter);

        if (Objects.nonNull(postCookie) && postCookie.getValue() != null) {
            String[] gatheringDataArray = URLDecoder.decode(postCookie.getValue(), StandardCharsets.UTF_8).split(",");
            boolean isUpdated = false;
            boolean hasExistingData = false;

            for (int i = 0; i < gatheringDataArray.length; i++) {
                String[] parts = gatheringDataArray[i].split("\\|");

                if (parts.length == 2) {
                    if (HmacUtils.verifyHmac(parts[0], parts[1])) {
                        String[] cookieInfo = parts[0].split("_");
                        Long cookieUserId = Long.parseLong(cookieInfo[0]);
                        Long cookieGatheringId = Long.parseLong(cookieInfo[1]);
                        LocalDate lastViewedAt = LocalDate.parse(cookieInfo[2], formatter);

                        if (cookieUserId.equals(userId) && cookieGatheringId.equals(gathering.getId())) {
                            hasExistingData = true;
                            if (lastViewedAt.isBefore(now.minusDays(1))) {
                                incrementGatheringViewCount(gathering);
                                String newHmac = HmacUtils.generateHmac(cookieData);
                                gatheringDataArray[i] = cookieData + "|" + newHmac;
                                isUpdated = true;
                            }
                            break;
                        }
                    }

                }
            }

            if (isUpdated || !hasExistingData) {
                if (!hasExistingData) {
                    incrementGatheringViewCount(gathering);
                }
                String hmac = HmacUtils.generateHmac(cookieData);
                String updatedValue = String.join(",", gatheringDataArray) + "," + cookieData + "|" + hmac;
                postCookie.setValue(URLEncoder.encode(updatedValue, StandardCharsets.UTF_8));
                postCookie.setPath("/");
                response.addCookie(postCookie);
            }


        } else {
            incrementGatheringViewCount(gathering);
            String hmac = HmacUtils.generateHmac(cookieData);
            Cookie newCookie = new Cookie(cookieName, URLEncoder.encode(cookieData + "|" + hmac, StandardCharsets.UTF_8));
            newCookie.setMaxAge(60 * 60 * 24);
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }

    private void incrementGatheringViewCount(Gathering gathering) {
        gathering.upViewCount();
    }


}
