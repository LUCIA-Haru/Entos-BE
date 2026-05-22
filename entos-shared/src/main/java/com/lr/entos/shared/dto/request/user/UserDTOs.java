package com.lr.entos.shared.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public sealed interface UserDTOs {


    record Update(
            @Size(min = 3, max = 50)
            String username,

            @Email
            String email,

            Boolean active
    ) implements UserDTOs {}

    record UpdatePassword(
            @NotBlank
            String oldPassword,

            @NotBlank
            @Size(min = 8)
            String newPassword
    ) implements UserDTOs {}

    record ResetPassword(
            @Email
            String email,
            @NotBlank
            @Size(min = 8)
            String newPassword
    ) implements UserDTOs{}


}
