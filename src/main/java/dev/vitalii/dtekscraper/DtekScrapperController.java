package dev.vitalii.dtekscraper;

import dev.vitalii.dtekscraper.integration.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1")
@RestController
public class DtekScrapperController {

    private final DtekScraperService service;


    @GetMapping("get-streets")
    public StreetsResponse getStreets() {
        return service.getStreets();
    }

    @GetMapping("home-num")
    public HomeNumResponse homeNum(@Valid HomeNumRequest request) {
        return service.getHomeNum(request);
    }

    @GetMapping("check-discon-update")
    public CheckDisconUpdateResponse checkDisconUpdate(@Valid CheckDisconUpdateRequest request) {
        return service.checkDisconUpdate(request);
    }
}
