package org.example.kbase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(

        @NotBlank(message = "Project name required")
        String name,

        String description
) {
}
