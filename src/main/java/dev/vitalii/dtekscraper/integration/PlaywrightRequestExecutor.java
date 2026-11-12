package dev.vitalii.dtekscraper.integration;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import dev.vitalii.dtekscraper.integration.exception.DtekException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
class PlaywrightRequestExecutor {

    private final BrowserProperties properties;
    private final ImpervaSessionManager sessionManager;


    String executeRequest(FormData formData) {
        APIResponse response = request(formData);

        // If the session expired (403 Forbidden), refresh it and retry exactly once
        if (response.status() == 403) {
            log.info("Session expired (403). Forcing refresh...");
            sessionManager.refreshSession();

            // Retry
            response = request(formData);
        }

        if (!response.ok()) {
            throw new DtekException(response.status(), response.text());
        }

        return response.text();
    }

    // Extracted the actual HTTP call to keep the retry logic clean
    APIResponse request(FormData formData) {
        return sessionManager.getActiveContext().request()
                .post(
                        properties.requestUrl(),
                        RequestOptions.create()
                                .setHeader("Accept", properties.headers().accept())
                                .setHeader("Content-Type", properties.headers().contentType())
                                .setHeader("Referer", properties.browserUrl())
                                .setHeader("X-Requested-With", "XMLHttpRequest")
                                .setHeader("X-CSRF-Token", sessionManager.getCsrfToken())
                                .setForm(formData)
                );
    }
}
