package org.example.kbase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(

        @NotBlank(message = "Project name required")
        String name,
        String description
) {
}
