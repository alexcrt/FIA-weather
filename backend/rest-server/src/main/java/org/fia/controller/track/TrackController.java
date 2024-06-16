package org.fia.controller.track;

import org.fia.controller.dto.track.TrackResponse;
import org.fia.service.TrackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService seasonService) {
        this.trackService = seasonService;
    }

    @GetMapping("/all")
    public List<TrackResponse> all() {
        return this.trackService.getAllTracks()
            .stream()
            .map(track -> new TrackResponse(track.getName()))
            .toList();
    }
}