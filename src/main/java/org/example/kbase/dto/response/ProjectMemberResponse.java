package org.example.kbase.dto.response;

import java.util.UUID;

public record ProjectMemberResponse(
        UUID id,
        UUID projectId,
        UUID userId,
        String role
) {
}