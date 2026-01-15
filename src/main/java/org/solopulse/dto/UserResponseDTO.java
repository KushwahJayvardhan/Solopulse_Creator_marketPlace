package org.solopulse.dto;

import lombok.*;
import org.solopulse.enums.Roles;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;

    private Roles userRole;
    private String imageUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer creatorProfileId;
    private Integer brandProfileId;
    private Integer marketerProfileId;
    private Integer profileId;
    private Integer proposalId;
}
