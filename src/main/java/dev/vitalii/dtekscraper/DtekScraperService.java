package dev.vitalii.dtekscraper;

import com.microsoft.playwright.APIResponse;
import dev.vitalii.dtekscraper.playwright.PlaywrightFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Log4j2
@RequiredArgsConstructor
@Service
public class DtekScraperService {

    private final ObjectMapper objectMapper;
    private final PlaywrightFactory playwrightFactory;


    public StreetsResponse getStreetsData() {
        APIResponse response = playwrightFactory.executeStreetsRequest();

        // If the session expired (403 Forbidden), refresh it and retry exactly once
        if (response.status() == 403) {
            log.info("Session expired (403). Forcing refresh...");
            playwrightFactory.refreshSession();

            // Retry
            response = playwrightFactory.executeStreetsRequest();
        }

        if (!response.ok()) {
            throw new RuntimeException("API request failed with HTTP " + response.status() + ": " + response.text());
        }

        try {
            return objectMapper.readValue(response.text(), StreetsResponse.class);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}
