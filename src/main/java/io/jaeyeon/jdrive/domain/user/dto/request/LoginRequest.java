package io.jaeyeon.jdrive.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        String email,

        @NotBlank
        String password
) { }
