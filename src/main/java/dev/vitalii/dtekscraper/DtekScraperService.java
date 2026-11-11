package dev.vitalii.dtekscraper;

import dev.vitalii.dtekscraper.integration.*;
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
        final String response = playwrightRequestExecutor
                .executeRequest(FormDataProducer.forGetStreets());

        try {
            return objectMapper.readValue(response, StreetsResponse.class);
        } catch (JacksonException e) {
            throw new DtekException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public HomeNumResponse getHomeNum(HomeNumRequest request) {
        final String response = playwrightRequestExecutor
                .executeRequest(FormDataProducer.forGetHomeNum(request.street(), request.houseNumber()));

        try {
            return objectMapper.readValue(response, HomeNumResponse.class);
        } catch (JacksonException e) {
            throw new DtekException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
