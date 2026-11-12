package dev.vitalii.dtekscraper.integration;

import com.microsoft.playwright.options.FormData;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class FormDataProducer {

    private static final String METHOD = "method";


    public static FormData forGetStreets() {
        return FormData.create()
                .append(METHOD, "getStreets");
    }

    public static FormData forGetHomeNum(String street, String houseNumber, String updateFact) {
        return FormData.create()
                .append(METHOD, "getHomeNum")
                .append("data[0][name]", "street")
                .append("data[0][value]", street)
                .append("data[1][name]", "house_num")
                .append("data[1][value]", houseNumber)
                .append("data[2][name]", "updateFact")
                .append("data[2][value]", updateFact);
    }

    public static FormData forCheckDisconUpdate() {
        return FormData.create()
                .append(METHOD, "checkDisconUpdate")
                .append("update", getNow());
    }

    private static String getNow() {
        return LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
