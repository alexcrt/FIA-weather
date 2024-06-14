package org.fia.controller.season;

import org.fia.controller.dto.season.SeasonResponse;
import org.fia.service.SeasonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/season")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping("/all")
    public List<SeasonResponse> all() {
        return this.seasonService.getAllSeasons()
            .stream()
            .map(s -> new SeasonResponse(s.getUuid().toString(), s.getStartingYear()))
            .toList();
    }
}