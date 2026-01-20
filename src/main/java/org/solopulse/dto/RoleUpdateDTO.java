package org.solopulse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.solopulse.enums.Roles;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateDTO {
    private Roles role;

    public Roles getRole(){
        return role;
    }

    public void setRole(Roles role){
        this.role = role;
    }
}
