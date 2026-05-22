package com.lr.entos.shared.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String username,
        @Email String email,
        @NotBlank String password

) {
}
