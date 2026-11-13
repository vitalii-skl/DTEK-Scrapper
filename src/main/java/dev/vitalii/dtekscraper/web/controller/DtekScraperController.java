package dev.vitalii.dtekscraper.web.controller;

import dev.vitalii.dtekscraper.integration.DtekScraperService;
import dev.vitalii.dtekscraper.integration.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/dtek-integration/v1")
@RestController
public class DtekScraperController {

    private final DtekScraperService service;


    @GetMapping("/getStreets")
    public StreetsResponse getStreets() {
        return service.getStreets();
    }

    @GetMapping("/getHomeNum")
    public HomeNumResponse homeNum(@Valid HomeNumRequest request) {
        return service.getHomeNum(request);
    }

    @GetMapping("/checkDisconUpdate")
    public CheckDisconUpdateResponse checkDisconUpdate(@Valid CheckDisconUpdateRequest request) {
        return service.checkDisconUpdate(request);
    }
}
