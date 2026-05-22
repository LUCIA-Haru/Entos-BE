package com.lr.entos.shared.dto.request.role;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoleDTOs {
    public record Create(
            @Size(min = 3, max = 50)
            @NotBlank String name,
            @NotBlank String type,
            @NotBlank String description
    ) {}

    public record Update(
            @Size(min = 3, max = 50)
            String name,
            Boolean status,
            String type,
            String description
    ) {}
}
