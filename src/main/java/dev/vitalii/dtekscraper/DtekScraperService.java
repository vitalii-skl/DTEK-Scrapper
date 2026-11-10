package dev.vitalii.dtekscraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.RequestOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private String csrfToken;


    @PostConstruct
    public void initSession() {
        playwright = Playwright.create();

        // Keep the main browser engine running permanently
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        // Establish the initial session
        refreshSession();
    }

    /**
     * Synchronized prevents multiple threads from trying to refresh the session at the exact same time if several API calls fail simultaneously.
     */
    private synchronized void refreshSession() {
        log.info("Establishing/Refreshing Imperva session...");

        // CRITICAL FOR UPTIME: Close the old browser context to prevent severe memory leaks
        if (context != null) {
            context.close();
        }

        context = browser.newContext(new Browser.NewContextOptions().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:159.0) Gecko/20100101 Firefox/159.0"));

        final Page page = context.newPage();
        page.navigate("https://www.dtek-kem.com.ua/ua/shutdowns");

        page.waitForLoadState(LoadState.NETWORKIDLE);

        // 3-second buffer for background WAF scripts:
        page.waitForTimeout(3000);

        csrfToken = page.locator("meta[name='csrf-token']").getAttribute("content");

        log.info("Session active. New CSRF Token: {}", csrfToken);

        // Close the tab to free memory; cookies stay in the context:
        page.close();
    }

    public StreetsResponse getStreetsData() {
        APIResponse response = executeStreetsRequest();

        // If the session expired (403 Forbidden), refresh it and retry exactly once
        if (response.status() == 403) {
            log.info("Session expired (403). Forcing refresh...");
            refreshSession();

            // Retry
            response = executeStreetsRequest();
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

    // Extracted the actual HTTP call to keep the retry logic clean
    private APIResponse executeStreetsRequest() {
        return context.request()
                .post(
                        "https://www.dtek-kem.com.ua/ua/ajax",
                        RequestOptions.create()
                                .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                                .setHeader("X-Requested-With", "XMLHttpRequest")
                                .setHeader("X-CSRF-Token", csrfToken)
                                .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .setHeader("Referer", "https://www.dtek-kem.com.ua/ua/shutdowns")
                                .setData("method=getStreets")
                );
    }

    @PreDestroy
    public void cleanup() {
        if (context != null) {
            context.close();
        }

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }

    static void main() {
        final DtekScraperService service = new DtekScraperService(new ObjectMapper());

        service.initSession();
        System.out.println(service.getStreetsData());
        service.cleanup();
    }
}
