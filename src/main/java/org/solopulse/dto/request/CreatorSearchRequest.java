package org.solopulse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorSearchRequest {

    private String location;
    private Long minFollowers;
    private Long maxFollowers;
    private Double minEngagementRate;
    private Double maxEngagementRate;
    private String pricingTier;
    private List<Platform> platforms;
    private List<ContentFormat> contentFormats;
    private String bioKeyword;

    // Pagination & Sorting
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "id";

    @Builder.Default
    private String sortDirection = "ASC";
}