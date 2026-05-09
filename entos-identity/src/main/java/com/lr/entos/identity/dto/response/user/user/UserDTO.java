package com.lr.entos.identity.dto.response.user.user;

import com.lr.entos.identity.dto.response.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private Set<RoleDTO> roles;
    private boolean active;


}
