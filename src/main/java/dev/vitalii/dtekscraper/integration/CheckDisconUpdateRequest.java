package dev.vitalii.dtekscraper.integration;

import jakarta.annotation.Nullable;

public record CheckDisconUpdateRequest(
        @Nullable String update
) {
}
