package dev.vitalii.dtekscraper.integration;

import java.util.List;
import java.util.Map;

public record CheckDisconUpdateResponse(
        boolean result,
        Fact fact,
        Preset preset,
        boolean showTableSchedule,
        boolean showUserGroup
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
            int today
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
