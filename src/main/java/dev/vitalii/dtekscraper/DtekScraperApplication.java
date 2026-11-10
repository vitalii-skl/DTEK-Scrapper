package dev.vitalii.dtekscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DtekScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(DtekScraperApplication.class, args);
    }

}
