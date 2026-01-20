package org.solopulse.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreatorProfileRequest {

    @Size(max = 100, message = "Instagram handle must not exceed 100 characters")
    private String instaHandle;

    @Size(max = 100, message = "YouTube handle must not exceed 100 characters")
    private String youtubeHandle;

    @Size(max = 100, message = "Twitter handle must not exceed 100 characters")
    private String twitterHandle;

    @Size(max = 100, message = "LinkedIn handle must not exceed 100 characters")
    private String linkedInHandle;

    @Size(max = 100, message = "Facebook handle must not exceed 100 characters")
    private String facebookHandle;

    @Size(max = 5000, message = "Bio must not exceed 5000 characters")
    private String bio;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @Min(value = 0, message = "Followers count must be non-negative")
    private Long followersCount;

    @DecimalMin(value = "0.0", message = "Engagement rate must be non-negative")
    @DecimalMax(value = "100.0", message = "Engagement rate must not exceed 100")
    private Double engagementRate;

    private List<ContentFormat> contentFormats;

    private String pricingTier;

    @Pattern(regexp = "^(https?://)?([\\w.-]+)\\.([a-z]{2,})(:[0-9]+)?(/.*)?$|^$",
            message = "Invalid URL format for availability calendar")
    private String availabilityCalendarUrl;

    private List<Platform> platforms;
}