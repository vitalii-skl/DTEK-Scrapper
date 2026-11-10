package dev.vitalii.dtekscraper.integration;

import java.util.List;

public record StreetsResponse(
        boolean result,
        List<String> streets
) {
}
