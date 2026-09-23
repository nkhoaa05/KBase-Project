package org.example.kbase.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.kbase.model.Enum.ProjectRole;

import java.util.UUID;

public record CreateProjectMemberRequest(

        @NotNull(message = "User ID required")
        UUID userId,

        @NotNull(message = "Project role required")
        ProjectRole role
) {
}