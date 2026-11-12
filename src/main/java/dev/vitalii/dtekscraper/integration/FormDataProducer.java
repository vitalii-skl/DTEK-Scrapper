package dev.vitalii.dtekscraper.integration;

import com.microsoft.playwright.options.FormData;
import lombok.experimental.UtilityClass;

@UtilityClass
class FormDataProducer {

    private static final String METHOD = "method";


    static FormData forGetStreets() {
        return FormData.create()
                .append(METHOD, "getStreets");
    }

    static FormData forGetHomeNum(String street, String houseNumber, String updateFact) {
        return FormData.create()
                .append(METHOD, "getHomeNum")
                .append("data[0][name]", "street")
                .append("data[0][value]", street)
                .append("data[1][name]", "house_num")
                .append("data[1][value]", houseNumber)
                .append("data[2][name]", "updateFact")
                .append("data[2][value]", updateFact);
    }

    static FormData forCheckDisconUpdate(String update) {
        return FormData.create()
                .append(METHOD, "checkDisconUpdate")
                .append("update", update);
    }
}
