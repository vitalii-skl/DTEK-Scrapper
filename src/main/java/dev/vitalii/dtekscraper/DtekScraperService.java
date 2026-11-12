package dev.vitalii.dtekscraper;

import dev.vitalii.dtekscraper.integration.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Log4j2
@RequiredArgsConstructor
@Service
public class DtekScraperService {

    private final ObjectMapper objectMapper;
    private final PlaywrightRequestExecutor playwrightRequestExecutor;


    public StreetsResponse getStreets() {
        final String response = playwrightRequestExecutor
                .executeRequest(FormDataProducer.forGetStreets());

        return objectMapper.readValue(response, StreetsResponse.class);
    }

    public HomeNumResponse getHomeNum(HomeNumRequest request) {
        final String response = playwrightRequestExecutor
                .executeRequest(FormDataProducer.forGetHomeNum(request.street(), request.houseNum(), request.updateFact()));

        return objectMapper.readValue(response, HomeNumResponse.class);
    }

    public CheckDisconUpdateResponse checkDisconUpdate(CheckDisconUpdateRequest request) {
        final String response = playwrightRequestExecutor
                .executeRequest(FormDataProducer.forCheckDisconUpdate(request.update()));

        return objectMapper.readValue(response, CheckDisconUpdateResponse.class);
    }
}
