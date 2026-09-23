package org.example.kbase.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.kbase.model.Enum.ProjectRole;

public record UpdateProjectMemberRequest(

        @NotNull(message = "Project role required")
        ProjectRole role
) {
}