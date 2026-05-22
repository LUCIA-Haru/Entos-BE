package com.lr.entos.shared.dto.response.user;

import com.lr.entos.shared.dto.response.role.RoleResponse;


import java.util.UUID;


public record UserResponse(
                           UUID guid,
                           String username,
                           String email,
                           RoleResponse role,
                           Boolean active) {



}
