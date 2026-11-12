package dev.vitalii.dtekscraper.integration.dto;

import java.util.List;

public record StreetsResponse(
        Boolean result,
        List<String> streets
) {
}
