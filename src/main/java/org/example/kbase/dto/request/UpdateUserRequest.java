package org.example.kbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must contain at least 8 - 100 characters")
        String password
) {
}
