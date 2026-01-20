package org.solopulse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorProfileResponse {

    private Integer id;
    private String instaHandle;
    private String youtubeHandle;
    private String twitterHandle;
    private String linkedInHandle;
    private String facebookHandle;
    private String bio;
    private String location;
    private Long followersCount;
    private Double engagementRate;
    private List<ContentFormat> contentFormats;
    private String pricingTier;
    private String availabilityCalendarUrl;
    private List<Platform> platforms;
    private Integer portfolioItemsCount;
    private Integer collaborationProjectsCount;

    // User information
    private UserBasicInfo user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserBasicInfo {
        private Integer id;
        private String name;
        private String email;
        private String profileImageUrl;
    }
}
