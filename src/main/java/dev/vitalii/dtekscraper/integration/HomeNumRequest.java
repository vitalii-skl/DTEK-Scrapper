package dev.vitalii.dtekscraper.integration;

import jakarta.validation.constraints.NotEmpty;

public record HomeNumRequest(
        @NotEmpty String street,
        @NotEmpty String houseNumber
) {
}
