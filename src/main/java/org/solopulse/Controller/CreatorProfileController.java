package org.solopulse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solopulse.dto.request.CreateCreatorProfileRequest;
import org.solopulse.dto.request.CreatorSearchRequest;
import org.solopulse.dto.request.UpdateCreatorProfileRequest;
import org.solopulse.dto.response.CreatorProfileResponse;
import org.solopulse.dto.response.CreatorStatsResponse;
import org.solopulse.dto.response.PageResponse;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;
import org.solopulse.service.CreatorProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creator-profiles")
@RequiredArgsConstructor
@Slf4j
public class CreatorProfileController {

    private final CreatorProfileService creatorProfileService;

    // ==================== CREATE ====================

    @PostMapping
    public ResponseEntity<CreatorProfileResponse> createProfile(
            @RequestBody CreateCreatorProfileRequest request) {
        log.info("API call to create creator profile");
        CreatorProfileResponse response = creatorProfileService.createProfile(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ==================== UPDATE ====================

    @PutMapping("/{profileId}")
    public ResponseEntity<CreatorProfileResponse> updateProfile(
            @PathVariable Integer profileId,
            @RequestBody UpdateCreatorProfileRequest request) {
        log.info("API call to update creator profile with ID: {}", profileId);
        CreatorProfileResponse response =
                creatorProfileService.updateProfile(profileId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{profileId}/metrics")
    public ResponseEntity<CreatorProfileResponse> updateMetrics(
            @PathVariable Integer profileId,
            @RequestParam(required = false) Long followersCount,
            @RequestParam(required = false) Double engagementRate) {
        log.info("API call to update metrics for profile ID: {}", profileId);
        CreatorProfileResponse response =
                creatorProfileService.updateMetrics(profileId, followersCount, engagementRate);
        return ResponseEntity.ok(response);
    }

    // ==================== READ ====================

    @GetMapping("/{profileId}")
    public ResponseEntity<CreatorProfileResponse> getProfileById(
            @PathVariable Integer profileId) {
        log.info("API call to get creator profile by ID: {}", profileId);
        CreatorProfileResponse response =
                creatorProfileService.getProfileById(profileId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CreatorProfileResponse> getProfileByUserId(
            @PathVariable Integer userId) {
        log.info("API call to get creator profile by user ID: {}", userId);
        CreatorProfileResponse response =
                creatorProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getAllProfiles(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        log.info("API call to get all creator profiles");
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getAllProfiles(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    // ==================== DELETE ====================

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Integer profileId) {
        log.info("API call to delete creator profile with ID: {}", profileId);
        creatorProfileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

    // ==================== SEARCH & FILTER ====================

    @PostMapping("/search")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> searchCreators(
            @RequestBody CreatorSearchRequest request) {
        log.info("API call to search creators");
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.searchCreators(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/location")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getCreatorsByLocation(
            @RequestParam String location,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getCreatorsByLocation(location, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/platform")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getCreatorsByPlatform(
            @RequestParam Platform platform,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getCreatorsByPlatform(platform, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/content-format")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getCreatorsByContentFormat(
            @RequestParam ContentFormat contentFormat,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getCreatorsByContentFormat(contentFormat, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pricing-tier")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getCreatorsByPricingTier(
            @RequestParam String pricingTier,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getCreatorsByPricingTier(pricingTier, page, size);
        return ResponseEntity.ok(response);
    }

    // ==================== RANKING ====================

    @GetMapping("/top/followers")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getTopCreatorsByFollowers(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getTopCreatorsByFollowers(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top/engagement")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getTopCreatorsByEngagement(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getTopCreatorsByEngagement(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<PageResponse<CreatorProfileResponse>> getFeaturedCreators(
            @RequestParam Double minEngagementRate,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PageResponse<CreatorProfileResponse> response =
                creatorProfileService.getFeaturedCreators(minEngagementRate, page, size);
        return ResponseEntity.ok(response);
    }

    // ==================== STATS ====================

    @GetMapping("/stats")
    public ResponseEntity<CreatorStatsResponse> getCreatorStats() {
        log.info("API call to fetch creator statistics");
        CreatorStatsResponse response =
                creatorProfileService.getCreatorStats();
        return ResponseEntity.ok(response);
    }

    // ==================== UTILITIES ====================

    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Integer userId) {
        boolean exists = creatorProfileService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/handle-available")
    public ResponseEntity<Boolean> isHandleAvailable(
            @RequestParam String platform,
            @RequestParam String handle) {
        boolean available = creatorProfileService.isHandleAvailable(platform, handle);
        return ResponseEntity.ok(available);
    }
}
