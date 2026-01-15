package org.solopulse.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.solopulse.enums.Roles;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;  // Password IS needed for creation

    @NotNull(message = "Role cannot be null")
    private Roles role;

    private String imageUrl;

    private String bio;

}

