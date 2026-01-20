package org.solopulse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorStatsResponse {

    private Long totalCreators;
    private Double averageEngagementRate;
    private Long totalFollowers;
    private Integer activeCreatorsThisMonth;
}
