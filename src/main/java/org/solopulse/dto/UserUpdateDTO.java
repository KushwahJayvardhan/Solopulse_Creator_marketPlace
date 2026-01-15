package org.solopulse.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private String bio;

}
