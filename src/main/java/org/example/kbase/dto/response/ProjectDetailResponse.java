package org.example.kbase.dto.response;

import java.util.List;
import java.util.UUID;

public record ProjectDetailResponse(
        UUID id,
        String name,
        String description,
        List<ProjectMemberResponse> members
) {
}