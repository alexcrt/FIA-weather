package org.fia.controller.weather;

import org.fia.controller.dto.weather.GrandPrixWeatherResponse;
import org.fia.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/grand-prix")
    public ResponseEntity<GrandPrixWeatherResponse> weatherData(@RequestParam UUID grandPrixUuid) {
        return this.weatherService.weatherResult(grandPrixUuid)
            .map(grandPrixWeatherResult -> ResponseEntity.ok(new GrandPrixWeatherResponse(grandPrixWeatherResult)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
