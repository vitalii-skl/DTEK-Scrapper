package dev.vitalii.dtekscraper.playwright;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.RequestOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class PlaywrightFactory {

    private final BrowserProperties properties;
    private final ObjectProvider<BrowserContext> contextProvider;

    private BrowserContext currentContext;
    private String csrfToken;


    @PostConstruct
    public void initSession() {
        // Establish the initial session
        refreshSession();
    }

    @PreDestroy
    public void cleanup() {
        if (currentContext != null) {
            currentContext.close();
        }
    }

    /**
     * Synchronized prevents multiple threads from trying to refresh the session at the exact same time if several API calls fail simultaneously.
     */
    public synchronized void refreshSession() {
        log.info("Establishing/Refreshing Imperva session...");

        // CRITICAL FOR UPTIME: Close the old browser context to prevent severe memory leaks
        if (currentContext != null) {
            currentContext.close();
        }

        currentContext = contextProvider.getObject();

        final Page page = currentContext.newPage();
        page.navigate(properties.browserUrl());

        page.waitForLoadState(LoadState.NETWORKIDLE);

        // 3-second buffer for background WAF scripts:
        page.waitForTimeout(3000);

        csrfToken = page.locator(properties.page().locator().value()).getAttribute(properties.page().locator().attribute());

        log.info("Session active. New CSRF Token: {}", csrfToken);

        // Close the tab to free memory; cookies stay in the context:
        page.close();
    }

    // Extracted the actual HTTP call to keep the retry logic clean
    public APIResponse executeStreetsRequest() {
        return currentContext.request()
                .post(
                        properties.requestUrl(),
                        RequestOptions.create()
                                .setHeader("Accept", properties.headers().accept())
                                .setHeader("Content-Type", properties.headers().contentType())
                                .setHeader("Referer", properties.browserUrl())
                                .setHeader("X-Requested-With", "XMLHttpRequest")
                                .setHeader("X-CSRF-Token", csrfToken)
                                .setData("method=getStreets")
                );
    }
}
