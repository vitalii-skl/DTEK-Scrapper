package dev.vitalii.dtekscraper.integration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
class PlaywrightConfig {

    @Bean(destroyMethod = "close")
    Playwright playwright() {
        return Playwright
                .create();
    }

    @Bean(destroyMethod = "close")
    Browser browser(Playwright playwright) {
        return playwright
                .chromium()
                .launch(
                        new BrowserType
                                .LaunchOptions()
                                .setHeadless(true)
                );
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BrowserContext browserContext(Browser browser, BrowserProperties browserProperties) {
        return browser.newContext(
                new Browser
                        .NewContextOptions()
                        .setUserAgent(browserProperties.userAgent())
        );
    }
}
