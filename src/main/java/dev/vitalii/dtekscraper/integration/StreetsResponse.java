package dev.vitalii.dtekscraper.integration;

import java.util.List;

public record StreetsResponse(
        Boolean result,
        List<String> streets
) {
}
