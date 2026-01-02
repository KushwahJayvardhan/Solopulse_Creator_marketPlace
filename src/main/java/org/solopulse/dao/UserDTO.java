package org.solopulse.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solopulse.enums.Roles;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;

    private String name;

    private String email;

    private Roles userRole;

    private String imageUrl;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Related profile references (IDs only)
    private Integer creatorProfileId;

    private Integer brandProfileId;

    private Integer marketerProfileId;

    private Integer profileId;

    private Integer proposalId;
}

