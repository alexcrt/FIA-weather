package org.fia.controller.airflow;

import org.fia.controller.dto.airflow.DagTriggerRequest;
import org.fia.controller.dto.airflow.GrandPrixWeatherRequest;
import org.fia.service.AirflowDagService;
import org.fia.service.GrandPrixService;
import org.fia.service.parameter.DagTriggerParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/airflow/dag")
public class AirflowDagController {

    private final AirflowDagService airflowDagService;
    private final GrandPrixService grandPrixService;

    public AirflowDagController(GrandPrixService grandPrixService, AirflowDagService airflowDagService) {
        this.airflowDagService = airflowDagService;
        this.grandPrixService = grandPrixService;
    }

    @GetMapping("/all")
    public Map<String, Object> listDags() {
        return this.airflowDagService.listDags();
    }

    @PostMapping("/trigger")
    public Map<String, Object> triggerDag(@RequestBody DagTriggerRequest dagTriggerRequest) {
        DagTriggerParameter dagTriggerParameter =
            new DagTriggerParameter(dagTriggerRequest.requestForecast(), dagTriggerRequest.latitude(), dagTriggerRequest.longitude(),
                dagTriggerRequest.startDate(), dagTriggerRequest.endDate());
        return this.airflowDagService.triggerDag(dagTriggerParameter);
    }

    @PostMapping("/weather")
    public ResponseEntity<List<Map<String, Object>>> triggerDag(@RequestBody GrandPrixWeatherRequest grandPrixWeatherRequest) {
        return this.grandPrixService.findById(grandPrixWeatherRequest.grandPrixUuid())
            .map(grandPrix -> ResponseEntity.ok(this.airflowDagService.triggerDags(grandPrix)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}