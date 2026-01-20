package org.solopulse.dto;

import lombok.*;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatorResponseDTO {
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

    // Reference info only, not full User object
    private Integer userId;
}
