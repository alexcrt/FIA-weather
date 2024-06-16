package org.fia.controller.grandprix;

import org.fia.controller.dto.grandprix.AddGrandPrixRequest;
import org.fia.controller.dto.grandprix.GrandPrixResponse;
import org.fia.service.GrandPrixService;
import org.fia.service.parameter.AddGrandPrixParameter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
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
    public List<GrandPrixResponse> findAll(@RequestParam UUID seasonUuid) {
        return this.grandPrixService.findAllBySeasonId(seasonUuid).stream().map(gp -> new GrandPrixResponse(gp.getUuid(), gp.getTrack().getName())).toList();
    }

    @PostMapping("/add")
    public void addGrandPrix(@RequestBody @Valid AddGrandPrixRequest addGrandPrixRequest) {
        int seasonYear = addGrandPrixRequest.getRaceStartingDateTime().getYear();
        AddGrandPrixParameter addGrandPrixParameter;
        if (addGrandPrixRequest.isSprint()) {
            addGrandPrixParameter = new AddGrandPrixParameter(
                seasonYear,
                addGrandPrixRequest.getTrackName(),
                addGrandPrixRequest.getTrackLatitude(),
                addGrandPrixRequest.getTrackLongitude(),
                addGrandPrixRequest.getTrackZoneOffsetHours(),
                true,
                addGrandPrixRequest.getFp1StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getFp1Duration()),
                null, null,
                null, null,
                addGrandPrixRequest.getSq1StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getSq1Duration()),
                addGrandPrixRequest.getSq2StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getSq2Duration()),
                addGrandPrixRequest.getSq3StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getSq3Duration()),
                addGrandPrixRequest.getSprintRaceStartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getSprintRaceDuration()),
                addGrandPrixRequest.getQ1StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ1Duration()),
                addGrandPrixRequest.getQ2StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ2Duration()),
                addGrandPrixRequest.getQ3StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ3Duration()),
                addGrandPrixRequest.getRaceStartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getRaceDuration())
            );
        } else {
            addGrandPrixParameter = new AddGrandPrixParameter(
                seasonYear,
                addGrandPrixRequest.getTrackName(),
                addGrandPrixRequest.getTrackLatitude(),
                addGrandPrixRequest.getTrackLongitude(),
                addGrandPrixRequest.getTrackZoneOffsetHours(),
                false,
                addGrandPrixRequest.getFp1StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getFp1Duration()),
                addGrandPrixRequest.getFp2StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getFp2Duration()),
                addGrandPrixRequest.getFp3StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getFp3Duration()),
                null, null,
                null, null,
                null, null,
                null, null,
                addGrandPrixRequest.getQ1StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ1Duration()),
                addGrandPrixRequest.getQ2StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ2Duration()),
                addGrandPrixRequest.getQ3StartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getQ3Duration()),
                addGrandPrixRequest.getRaceStartingDateTime(), Duration.ofMinutes(addGrandPrixRequest.getRaceDuration())
            );
        }
        this.grandPrixService.addGrandPrix(addGrandPrixParameter);
    }
}