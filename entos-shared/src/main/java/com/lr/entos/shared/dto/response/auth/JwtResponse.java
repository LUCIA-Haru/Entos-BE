package com.lr.entos.shared.dto.response.auth;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.*;

public record JwtResponse(
        @NotBlank String token,
       @NotNull UUID guid,
       @NotBlank String username,
       @Email String email,
       @NotEmpty List<String> roles
) {
}
