package dev.vitalii.dtekscraper.integration.dto;

import jakarta.annotation.Nullable;

public record CheckDisconUpdateRequest(
        @Nullable String update
) {
}
