package org.example.kbase.dto.response;

import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description
) {
}
