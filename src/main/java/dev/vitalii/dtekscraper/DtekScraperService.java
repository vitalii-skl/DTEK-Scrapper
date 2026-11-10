package dev.vitalii.dtekscraper;

import com.microsoft.playwright.options.FormData;
import dev.vitalii.dtekscraper.integration.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class DtekScraperService {

    private final ObjectMapper objectMapper;
    private final PlaywrightRequestExecutor playwrightRequestExecutor;

    private static final Map<MethodName, FormData> REQUESTS = Map.of(
            MethodName.GET_STREETS,
            FormData
                    .create()
                    .append(MethodName.KEY, MethodName.GET_STREETS.value()),

            MethodName.GET_HOME_NUM,
            FormData
                    .create()
                    .append(MethodName.KEY, MethodName.GET_HOME_NUM.value())
                    .append("data[0][name]", "street")
                    .append("data[0][value]", "*")
                    .append("data[1][name]", "house_num")
                    .append("data[1][value]", "*")
                    .append("data[2][name]", "updateFact")
                    .append("data[2][value]", LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))),

            MethodName.CHECK_DISCON_UPDATE,
            FormData
                    .create()
                    .append(MethodName.KEY, MethodName.CHECK_DISCON_UPDATE.value())
                    .append("update", LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
    );


    public StreetsResponse getStreetsData() {
        final FormData formData = REQUESTS.get(MethodName.GET_STREETS);
        final String response = playwrightRequestExecutor.executeRequest(formData);

        try {
            return objectMapper.readValue(response, StreetsResponse.class);
        } catch (JacksonException e) {
            throw new DtekException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public HomeNumResponse getHomeNum(HomeNumRequest request) {
        final FormData formData = REQUESTS.get(MethodName.GET_HOME_NUM);

        formData.set("data[0][value]", request.street());
        formData.set("data[1][value]", request.houseNumber());

        final String response = playwrightRequestExecutor.executeRequest(formData);

        try {
            return objectMapper.readValue(response, HomeNumResponse.class);
        } catch (JacksonException e) {
            throw new DtekException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}

enum MethodName {
    GET_STREETS("getStreets"),
    GET_HOME_NUM("getHomeNum"),
    CHECK_DISCON_UPDATE("checkDisconUpdate"),
    ;

    public static final String KEY = "method";
    private final String value;


    MethodName(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
