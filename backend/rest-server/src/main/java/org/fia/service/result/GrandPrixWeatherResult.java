package org.fia.service.result;

import lombok.Builder;
import org.fia.bean.WeatherDataReport;

import java.time.ZoneOffset;
import java.util.Map;

@Builder(builderMethodName = "")
public record GrandPrixWeatherResult(
    String trackName,
    ZoneOffset zoneOffset,
    String latitude,
    String longitude,
    boolean isSprint,
    Map<String, WeatherDataReport> weatherDataReportList) {
}
