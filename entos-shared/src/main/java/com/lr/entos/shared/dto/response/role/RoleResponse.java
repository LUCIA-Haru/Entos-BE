package com.lr.entos.shared.dto.response.role;

import java.util.UUID;

public record RoleResponse(
        UUID guid,
        String name,
        Boolean status,
        String type,
        String description
) {
}
