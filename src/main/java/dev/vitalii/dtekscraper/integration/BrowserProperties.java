package dev.vitalii.dtekscraper.integration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "playwright")
public record BrowserProperties(
        String browserUrl,
        String requestUrl,
        String userAgent,
        Headers headers,
        Page page
) {
    record Headers(
            String accept,
            String contentType
    ) {
    }

    record Page(Locator locator) {
    }

    record Locator(String value, String attribute) {
    }
}
