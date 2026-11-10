package dev.vitalii.dtekscraper.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StreetsResponse {

    private boolean result;
    private List<String> streets;
}
