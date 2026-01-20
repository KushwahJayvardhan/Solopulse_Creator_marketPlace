package org.solopulse.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.solopulse.entity.Portfolios;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatorCreateDTO {

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

    // Relationship field
    // We only pass userId while creating a creator profile
    private Integer userId;

}
