package dev.vitalii.dtekscraper;

import com.microsoft.playwright.options.FormData;
import dev.vitalii.dtekscraper.playwright.DtekException;
import dev.vitalii.dtekscraper.playwright.PlaywrightRequestExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Log4j2
@RequiredArgsConstructor
@Service
public class DtekScraperService {

    private final ObjectMapper objectMapper;
    private final PlaywrightRequestExecutor playwrightRequestExecutor;


    public StreetsResponse getStreetsData() {
        final FormData formData = FormData.create().append("method", "getStreets");
        final String response = playwrightRequestExecutor.executeRequest(formData);

        try {
            return objectMapper.readValue(response, StreetsResponse.class);
        } catch (JacksonException e) {
            throw new DtekException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
