package dev.vitalii.dtekscraper.integration;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HomeNumResponse(
        Boolean result,
        Boolean showCurOutageParam,
        Boolean showCurSchedule,
        Boolean showTableSchedule,
        Boolean showTablePlan,
        Boolean showTableFact,
        Boolean showUserGroup,
        String updateTimestamp,
        Map<String, Datum> data,
        Fact fact,
        Preset preset
) {
    record Datum(
            String sub_type,
            String start_date,
            String end_date,
            String type,
            List<String> sub_type_reason,
            Object voluntarily,
            Object cek
    ) {
    }

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
