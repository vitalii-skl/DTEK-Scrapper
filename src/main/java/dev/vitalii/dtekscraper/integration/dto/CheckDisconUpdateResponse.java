package dev.vitalii.dtekscraper.integration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CheckDisconUpdateResponse(
        Boolean result,
        Fact fact,
        Preset preset,
        Boolean showTableSchedule,
        Boolean showUserGroup
) {
    record Fact(
            Map<String, Map<String, Map<String, String>>> data,
            String update,
            Integer today
    ) {
    }

    record Preset(
            Map<String, String> days,
            Map<String, String> days_mini,
            Map<String, String> sch_names,
            Map<String, List<String>> time_zone,
            Map<String, String> time_type,
            Map<String, Map<String, Map<String, String>>> data,
            String updateFact
    ) {
    }
}
