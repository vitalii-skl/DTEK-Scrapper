package dev.vitalii.dtekscraper.integration;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

public record HomeNumRequest(
        @NotEmpty String street,
        @NotEmpty String houseNum,
        @Nullable String updateFact
) {
}
