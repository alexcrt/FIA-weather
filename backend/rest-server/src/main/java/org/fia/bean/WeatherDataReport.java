package org.fia.bean;

import java.util.Map;

public record WeatherDataReport(Map<String, TimeSeries> weatherData) {
    public boolean hasReports() {
        return !weatherData.isEmpty();
    }
}
