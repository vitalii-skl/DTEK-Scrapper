package dev.vitalii.dtekscraper.integration;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@DependsOn("browser")
@Component
class ImpervaSessionManager {

    private final BrowserProperties properties;
    private final ObjectProvider<BrowserContext> contextProvider;

    @Getter
    private BrowserContext activeContext;

    @Getter
    private String csrfToken;


    @PostConstruct
    void init() {
        establishSession();
    }

    @PreDestroy
    void cleanup() {
        if (activeContext != null) {
            try {
                activeContext.close();
            } catch (PlaywrightException e) {
                if (e.getMessage() != null && e.getMessage().contains("Target page, context or browser has been closed")) {
                    log.debug("Content was already closed by the global browser shutdown.");
                } else {
                    log.warn("Non-critical error while closing Imperva context: {}", e.getMessage());
                }
            }
        }
    }

    synchronized void refreshSession() {
        log.info("Refreshing Imperva session...");

        cleanup();  // CRITICAL FOR UPTIME: Close the old browser context (if open) to prevent severe memory leaks
        initSession();
    }

    private void establishSession() {
        log.info("Establishing Imperva session...");

        initSession();
    }

    private void initSession() {
        final BrowserContext freshContext = contextProvider.getObject();
        final String freshToken = acquireCsrfToken(freshContext, properties);

        activeContext = freshContext;
        csrfToken = freshToken;

        log.info("Imperva session active.");
    }

    private String acquireCsrfToken(BrowserContext context, BrowserProperties props) {
        try (final Page page = context.newPage()) {

            page.navigate(props.browserUrl());

            final Locator csrfLocator = page.locator(props.page().locator().value());

            csrfLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));

            final String token = csrfLocator.getAttribute(props.page().locator().attribute());

            log.info("CSRF Token acquired.");

            return token;
        }
    }
}
