package org.fia.controller.dto.airflow;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public record DagTriggerRequest(
    boolean requestForecast,
    @NotBlank String latitude,
    @NotBlank String longitude,
    @NotBlank LocalDate startDate,
    @NotBlank LocalDate endDate
) {
}