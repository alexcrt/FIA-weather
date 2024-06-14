package org.fia.controller.grandprix;

import org.fia.controller.dto.grandprix.GrandPrixResponse;
import org.fia.service.GrandPrixService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grandprix")
public class GrandPrixController {

    private final GrandPrixService grandPrixService;

    public GrandPrixController(GrandPrixService grandPrixService) {
        this.grandPrixService = grandPrixService;
    }

    @GetMapping("/all")
    public List<GrandPrixResponse> session(@RequestParam UUID seasonUuid) {
        return this.grandPrixService.findAllBySeasonId(seasonUuid).stream().map(gp -> new GrandPrixResponse(gp.getUuid(), gp.getTrack().getName())).toList();
    }

}