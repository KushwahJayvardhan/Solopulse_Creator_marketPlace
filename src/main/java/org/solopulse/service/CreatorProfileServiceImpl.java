package org.solopulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solopulse.dto.request.CreateCreatorProfileRequest;
import org.solopulse.dto.request.CreatorSearchRequest;
import org.solopulse.dto.request.UpdateCreatorProfileRequest;
import org.solopulse.dto.response.CreatorProfileResponse;
import org.solopulse.dto.response.CreatorStatsResponse;
import org.solopulse.dto.response.PageResponse;
import org.solopulse.entity.Creator_profile;
import org.solopulse.entity.User;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;
import org.solopulse.exception.BusinessValidationException;
import org.solopulse.exception.DuplicateResourceException;
import org.solopulse.exception.ResourceNotFoundException;
import org.solopulse.mapper.CreatorProfileMapper;
import org.solopulse.repository.CreatorProfileRepository;
import org.solopulse.repository.UserRepo;
import org.solopulse.service.CreatorProfileService;
import org.solopulse.specification.CreatorProfileSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CreatorProfileServiceImpl implements CreatorProfileService {

    private final CreatorProfileRepository creatorProfileRepository;
    private final UserRepo userRepository;
    private final CreatorProfileMapper mapper;

    @Override
    @Transactional
    @CacheEvict(value = {"creatorProfiles", "creatorStats"}, allEntries = true)
    public CreatorProfileResponse createProfile(CreateCreatorProfileRequest request) {
        log.info("Creating creator profile for user ID: {}", request.getUserId());

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + request.getUserId()));

        // Check if profile already exists for this user
        if (creatorProfileRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException(
                    "Creator profile already exists for user ID: " + request.getUserId());
        }

        // Validate social media handles uniqueness
        validateHandleUniqueness(request);

        // Validate business rules
        validateBusinessRules(request);

        // Map and save
        Creator_profile entity = mapper.toEntity(request, user);
        Creator_profile saved = creatorProfileRepository.save(entity);

        log.info("Successfully created creator profile with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"creatorProfiles", "creatorStats"}, allEntries = true)
    public CreatorProfileResponse updateProfile(Integer profileId, UpdateCreatorProfileRequest request) {
        log.info("Updating creator profile with ID: {}", profileId);

        Creator_profile existingProfile = creatorProfileRepository.findByIdWithDetails(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Creator profile not found with ID: " + profileId));

        // Validate social media handles if being updated
        validateHandleUniquenessForUpdate(profileId, request);

        // Validate business rules if relevant fields are updated
        validateBusinessRulesForUpdate(request);

        // Update entity
        mapper.updateEntity(request, existingProfile);
        Creator_profile updated = creatorProfileRepository.save(existingProfile);

        log.info("Successfully updated creator profile with ID: {}", profileId);
        return mapper.toResponse(updated);
    }

    @Override
    @Cacheable(value = "creatorProfiles", key = "#profileId")
    public CreatorProfileResponse getProfileById(Integer profileId) {
        log.info("Fetching creator profile with ID: {}", profileId);

        Creator_profile profile = creatorProfileRepository.findByIdWithDetails(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Creator profile not found with ID: " + profileId));

        return mapper.toResponse(profile);
    }

    @Override
    @Cacheable(value = "creatorProfiles", key = "'user-' + #userId")
    public CreatorProfileResponse getProfileByUserId(Integer userId) {
        log.info("Fetching creator profile for user ID: {}", userId);

        Creator_profile profile = creatorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Creator profile not found for user ID: " + userId));

        return mapper.toResponse(profile);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"creatorProfiles", "creatorStats"}, allEntries = true)
    public void deleteProfile(Integer profileId) {
        log.info("Deleting creator profile with ID: {}", profileId);

        if (!creatorProfileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException(
                    "Creator profile not found with ID: " + profileId);
        }

        creatorProfileRepository.deleteById(profileId);
        log.info("Successfully deleted creator profile with ID: {}", profileId);
    }

    @Override
    public PageResponse<CreatorProfileResponse> searchCreators(CreatorSearchRequest request) {
        log.info("Searching creators with filters: {}", request);

        // Build specification
        Specification<Creator_profile> spec = CreatorProfileSpecification.withFilters(
                request.getLocation(),
                request.getMinFollowers(),
                request.getMaxFollowers(),
                request.getMinEngagementRate(),
                request.getPricingTier(),
                request.getPlatforms(),
                request.getContentFormats(),
                request.getBioKeyword()
        );

        // Create pageable
        Pageable pageable = createPageable(
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );

        // Execute query
        Page<Creator_profile> page = creatorProfileRepository.findAll(spec, pageable);

        return convertToPageResponse(page);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getAllProfiles(
            Integer page, Integer size, String sortBy, String sortDirection) {
        log.info("Fetching all creator profiles - page: {}, size: {}", page, size);

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Creator_profile> profilePage = creatorProfileRepository.findAllWithDetails(pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getCreatorsByLocation(
            String location, Integer page, Integer size) {
        log.info("Fetching creators by location: {}", location);

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findByLocationContainingIgnoreCase(location, pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getCreatorsByPlatform(
            Platform platform, Integer page, Integer size) {
        log.info("Fetching creators by platform: {}", platform);

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findByPlatform(platform, pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getCreatorsByContentFormat(
            ContentFormat contentFormat, Integer page, Integer size) {
        log.info("Fetching creators by content format: {}", contentFormat);

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findByContentFormat(contentFormat, pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getTopCreatorsByFollowers(
            Integer page, Integer size) {
        log.info("Fetching top creators by followers");

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findTopCreatorsByFollowers(pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getTopCreatorsByEngagement(
            Integer page, Integer size) {
        log.info("Fetching top creators by engagement");

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findTopCreatorsByEngagement(pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    public PageResponse<CreatorProfileResponse> getFeaturedCreators(
            Double minEngagementRate, Integer page, Integer size) {
        log.info("Fetching featured creators with min engagement rate: {}", minEngagementRate);

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findFeaturedCreators(minEngagementRate, pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    @Cacheable(value = "creatorStats")
    public CreatorStatsResponse getCreatorStats() {
        log.info("Fetching creator statistics");

        Long totalCreators = creatorProfileRepository.count();

        // Calculate average engagement rate
        List<Creator_profile> allCreators = creatorProfileRepository.findAll();
        Double averageEngagementRate = allCreators.stream()
                .filter(c -> c.getEngagementRate() != null)
                .mapToDouble(Creator_profile::getEngagementRate)
                .average()
                .orElse(0.0);

        // Calculate total followers
        Long totalFollowers = allCreators.stream()
                .filter(c -> c.getFollowersCount() != null)
                .mapToLong(Creator_profile::getFollowersCount)
                .sum();

        return new CreatorStatsResponse(
                totalCreators,
                Math.round(averageEngagementRate * 100.0) / 100.0,
                totalFollowers,
                totalCreators.intValue() // Simplified - you can add actual logic
        );
    }

    @Override
    public boolean existsByUserId(Integer userId) {
        return creatorProfileRepository.existsByUserId(userId);
    }

    @Override
    public boolean isHandleAvailable(String platform, String handle) {
        if (handle == null || handle.isBlank()) {
            return true;
        }

        return switch (platform.toLowerCase()) {
            case "instagram" -> !creatorProfileRepository.existsByInstaHandle(handle);
            case "youtube" -> !creatorProfileRepository.existsByYoutubeHandle(handle);
            case "twitter" -> !creatorProfileRepository.existsByTwitterHandle(handle);
            default -> true;
        };
    }

    @Override
    public PageResponse<CreatorProfileResponse> getCreatorsByPricingTier(
            String pricingTier, Integer page, Integer size) {
        log.info("Fetching creators by pricing tier: {}", pricingTier);

        Pageable pageable = PageRequest.of(page, size);
        Page<Creator_profile> profilePage = creatorProfileRepository
                .findByPricingTier(pricingTier, pageable);

        return convertToPageResponse(profilePage);
    }

    @Override
    @Transactional
    @CacheEvict(value = "creatorProfiles", key = "#profileId")
    public CreatorProfileResponse updateMetrics(
            Integer profileId, Long followersCount, Double engagementRate) {
        log.info("Updating metrics for profile ID: {}", profileId);

        Creator_profile profile = creatorProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Creator profile not found with ID: " + profileId));

        if (followersCount != null && followersCount >= 0) {
            profile.setFollowersCount(followersCount);
        }

        if (engagementRate != null && engagementRate >= 0 && engagementRate <= 100) {
            profile.setEngagementRate(engagementRate);
        }

        Creator_profile updated = creatorProfileRepository.save(profile);
        return mapper.toResponse(updated);
    }

    // ==================== Private Helper Methods ====================

    private void validateHandleUniqueness(CreateCreatorProfileRequest request) {
        if (request.getInstaHandle() != null &&
                creatorProfileRepository.existsByInstaHandle(request.getInstaHandle())) {
            throw new DuplicateResourceException(
                    "Instagram handle already exists: " + request.getInstaHandle());
        }

        if (request.getYoutubeHandle() != null &&
                creatorProfileRepository.existsByYoutubeHandle(request.getYoutubeHandle())) {
            throw new DuplicateResourceException(
                    "YouTube handle already exists: " + request.getYoutubeHandle());
        }

        if (request.getTwitterHandle() != null &&
                creatorProfileRepository.existsByTwitterHandle(request.getTwitterHandle())) {
            throw new DuplicateResourceException(
                    "Twitter handle already exists: " + request.getTwitterHandle());
        }
    }

    private void validateHandleUniquenessForUpdate(Integer profileId, UpdateCreatorProfileRequest request) {
        if (request.getInstaHandle() != null) {
            creatorProfileRepository.findByInstaHandle(request.getInstaHandle())
                    .ifPresent(profile -> {
                        if (!profile.getId().equals(profileId)) {
                            throw new DuplicateResourceException(
                                    "Instagram handle already exists: " + request.getInstaHandle());
                        }
                    });
        }

        if (request.getYoutubeHandle() != null) {
            creatorProfileRepository.findByYoutubeHandle(request.getYoutubeHandle())
                    .ifPresent(profile -> {
                        if (!profile.getId().equals(profileId)) {
                            throw new DuplicateResourceException(
                                    "YouTube handle already exists: " + request.getYoutubeHandle());
                        }
                    });
        }

        if (request.getTwitterHandle() != null) {
            creatorProfileRepository.findByTwitterHandle(request.getTwitterHandle())
                    .ifPresent(profile -> {
                        if (!profile.getId().equals(profileId)) {
                            throw new DuplicateResourceException(
                                    "Twitter handle already exists: " + request.getTwitterHandle());
                        }
                    });
        }
    }

    private void validateBusinessRules(CreateCreatorProfileRequest request) {
        // Validate at least one social media handle
        if (isAllHandlesEmpty(
                request.getInstaHandle(),
                request.getYoutubeHandle(),
                request.getTwitterHandle(),
                request.getLinkedInHandle(),
                request.getFacebookHandle())) {
            throw new BusinessValidationException(
                    "At least one social media handle must be provided");
        }

        // Validate engagement rate range
        if (request.getEngagementRate() != null &&
                (request.getEngagementRate() < 0 || request.getEngagementRate() > 100)) {
            throw new BusinessValidationException("Engagement rate must be between 0 and 100");
        }

    }

    private void validateBusinessRulesForUpdate(UpdateCreatorProfileRequest request) {

        // Validate engagement rate if provided
        if (request.getEngagementRate() != null) {
            if (request.getEngagementRate() < 0 || request.getEngagementRate() > 100) {
                throw new BusinessValidationException(
                        "Engagement rate must be between 0 and 100");
            }
        }

        // Validate followers count if provided
        if (request.getFollowersCount() != null) {
            if (request.getFollowersCount() < 0) {
                throw new BusinessValidationException(
                        "Followers count cannot be negative");
            }
        }

        // Validate pricing tier if provided
        if (request.getPricingTier() != null && request.getPricingTier().isBlank()) {
            throw new BusinessValidationException(
                    "Pricing tier cannot be empty");
        }

        // Validate location if provided
        if (request.getLocation() != null && request.getLocation().isBlank()) {
            throw new BusinessValidationException(
                    "Location cannot be empty");
        }

        // Validate bio if provided
        if (request.getBio() != null && request.getBio().isBlank()) {
            throw new BusinessValidationException(
                    "Bio cannot be empty");
        }

        // Validate handles format if provided (basic sanity check)
        validateHandle(request.getInstaHandle(), "Instagram");
        validateHandle(request.getYoutubeHandle(), "YouTube");
        validateHandle(request.getTwitterHandle(), "Twitter");
        validateHandle(request.getLinkedInHandle(), "LinkedIn");
        validateHandle(request.getFacebookHandle(), "Facebook");
    }

    private void validateHandle(String handle, String platform) {
        if (handle != null && handle.isBlank()) {
            throw new BusinessValidationException(
                    platform + " handle cannot be empty");
        }
    }

    private boolean isAllHandlesEmpty(String... handles) {
        for (String handle : handles) {
            if (handle != null && !handle.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private Pageable createPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection != null && sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.by(direction, sortBy != null ? sortBy : "id")
        );
    }

    private PageResponse<CreatorProfileResponse> convertToPageResponse(Page<Creator_profile> page) {
        List<CreatorProfileResponse> content = page.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<CreatorProfileResponse>builder()
                .content(content)
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}
