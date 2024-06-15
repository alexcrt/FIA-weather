package org.fia.service.parameter;

import java.time.LocalDate;

public record DagTriggerParameter(
    boolean requestForecast,
    String latitude,
    String longitude,
    LocalDate startDate,
    LocalDate endDate
) {
}