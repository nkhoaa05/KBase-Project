package org.example.kbase.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record RemoveMemberRequest(

        @NotNull(message = "Project ID required")
        UUID projectId,

        @NotNull(message = "User ID required")
        UUID memberId
) {
}
