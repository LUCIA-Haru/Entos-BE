package com.lr.entos.identity.dto.records;

import java.util.List;
import jakarta.validation.constraints.*;

public record JwtResponse(
        @NotBlank String token,
       @NotNull  Long id,
       @NotBlank String username,
       @Email String email,
       @NotEmpty List<String> roles
) {
}
