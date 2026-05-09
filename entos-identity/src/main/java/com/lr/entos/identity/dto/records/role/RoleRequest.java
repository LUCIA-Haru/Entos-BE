package com.lr.entos.identity.dto.records.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleRequest(
        @NotNull UUID guid,
        @NotBlank String name,
        @NotNull boolean status,
        @NotBlank String type,
        @NotBlank String desciption
) {
}
