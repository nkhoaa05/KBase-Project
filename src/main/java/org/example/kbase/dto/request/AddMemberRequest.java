package org.example.kbase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.kbase.model.Enum.ProjectRole;

import java.util.UUID;

public record AddMemberRequest(

        @NotNull(message = "Project required")
        UUID projectId,

        @NotBlank(message = "Email required")
        @Email(message = "Invalid email format")
        String memberEmail,

        @NotNull(message = "User role required")
        ProjectRole role
) {
}
