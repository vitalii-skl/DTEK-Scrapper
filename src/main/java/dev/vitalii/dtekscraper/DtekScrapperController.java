package dev.vitalii.dtekscraper;

import dev.vitalii.dtekscraper.integration.HomeNumRequest;
import dev.vitalii.dtekscraper.integration.HomeNumResponse;
import dev.vitalii.dtekscraper.integration.StreetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1")
@RestController
public class DtekScrapperController {

    private final DtekScraperService service;


    @GetMapping("streets")
    public StreetsResponse streets() {
        return service.getStreetsData();
    }

    @GetMapping("home-num")
    public HomeNumResponse homeNum(HomeNumRequest request) {
        return service.getHomeNum(request);
    }
}
