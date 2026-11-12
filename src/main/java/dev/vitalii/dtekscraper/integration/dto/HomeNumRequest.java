package dev.vitalii.dtekscraper.integration.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

public record HomeNumRequest(
        @NotEmpty String street,
        @Nullable String houseNum,
        @Nullable String updateFact
) {
}
