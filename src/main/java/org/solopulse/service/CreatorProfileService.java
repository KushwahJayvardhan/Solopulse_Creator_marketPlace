package org.solopulse.service;

import org.solopulse.dto.request.CreateCreatorProfileRequest;
import org.solopulse.dto.request.CreatorSearchRequest;
import org.solopulse.dto.request.UpdateCreatorProfileRequest;
import org.solopulse.dto.response.CreatorProfileResponse;
import org.solopulse.dto.response.CreatorStatsResponse;
import org.solopulse.dto.response.PageResponse;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.List;

public interface CreatorProfileService {


    CreatorProfileResponse createProfile(CreateCreatorProfileRequest request);

    CreatorProfileResponse updateProfile(Integer profileId, UpdateCreatorProfileRequest request);

    CreatorProfileResponse getProfileById(Integer profileId);

    CreatorProfileResponse getProfileByUserId(Integer userId);

    void deleteProfile(Integer profileId);

    PageResponse<CreatorProfileResponse> searchCreators(CreatorSearchRequest request);

    PageResponse<CreatorProfileResponse> getAllProfiles(Integer page, Integer size, String sortBy, String sortDirection);

    PageResponse<CreatorProfileResponse> getCreatorsByLocation(String location, Integer page, Integer size);

    PageResponse<CreatorProfileResponse> getCreatorsByPlatform(Platform platform, Integer page, Integer size);

    PageResponse<CreatorProfileResponse> getCreatorsByContentFormat(ContentFormat contentFormat, Integer page, Integer size);

    PageResponse<CreatorProfileResponse> getTopCreatorsByFollowers(Integer page, Integer size);


    PageResponse<CreatorProfileResponse> getTopCreatorsByEngagement(Integer page, Integer size);

    PageResponse<CreatorProfileResponse> getFeaturedCreators(Double minEngagementRate, Integer page, Integer size);

    CreatorStatsResponse getCreatorStats();

    boolean existsByUserId(Integer userId);

    boolean isHandleAvailable(String platform, String handle);

    PageResponse<CreatorProfileResponse> getCreatorsByPricingTier(String pricingTier, Integer page, Integer size);

    CreatorProfileResponse updateMetrics(Integer profileId, Long followersCount, Double engagementRate);
}