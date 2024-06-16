package org.fia.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.fia.bean.TimeSeries;
import org.fia.bean.WeatherDataReport;
import org.fia.domain.GrandPrix;
import org.fia.domain.TimedSession;
import org.fia.domain.Track;
import org.fia.repository.GrandPrixRepository;
import org.fia.service.result.GrandPrixWeatherResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class WeatherService {

    private final AirflowDagService airflowDagService;
    private final GrandPrixRepository grandPrixRepository;
    private final InfluxDBClient influxDBClient;
    @Value("${influxdb.bucket}")
    private String influxDbBucket;

    @Value("${influxdb.org}")
    private String influxDbOrg;

    public WeatherService(AirflowDagService airflowDagService, GrandPrixRepository grandPrixRepository, InfluxDBClient influxDBClient) {
        this.airflowDagService = airflowDagService;
        this.grandPrixRepository = grandPrixRepository;
        this.influxDBClient = influxDBClient;
    }

    public Optional<GrandPrixWeatherResult> weatherResult(UUID grandPrixUuid) {
        Optional<GrandPrix> optionalGrandPrix = grandPrixRepository.findById(grandPrixUuid);
        if (optionalGrandPrix.isEmpty()) {
            return Optional.empty();
        }

        GrandPrix grandPrix = optionalGrandPrix.get();
        Track track = grandPrix.getTrack();

        // Use LinkedHashmap to keep insertion order
        Map<String, WeatherDataReport> weatherDataReportList = new LinkedHashMap<>();
        for (Map.Entry<String, TimedSession> timedSessionData : grandPrix.timedSessionsInOrder()) {
            TimedSession timedSession = timedSessionData.getValue();
            WeatherDataReport weatherDataReport = loadWeatherDataReport(track.getLatitude(), track.getLongitude(), timedSession);
            if (!weatherDataReport.hasReports()) {
                airflowDagService.triggerDag(grandPrix, timedSession);
            }
            weatherDataReportList.put(timedSessionData.getKey(), weatherDataReport);
        }
        return Optional.of(new GrandPrixWeatherResult(
            track.getName(),
            track.getZoneOffset(),
            track.getLatitude(),
            track.getLongitude(),
            grandPrix.isSprint(),
            weatherDataReportList)
        );
    }

    private WeatherDataReport loadWeatherDataReport(String latitude, String longitude, TimedSession session) {
        // From the historical weather API: You can access data dating back to 1940 with a delay of 2 days.
        long twoDaysAgo = LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC);

        String measurementFieldValue = "forecast";
        if (session.getEndingTimestamp() <= twoDaysAgo) {
            measurementFieldValue = "historical";
        }
        String query = """
               from(bucket: "%s")
               |> range(start: %s, stop: %s)
               |> filter(fn: (r) => r["_measurement"] == "%s")
               |> filter(fn: (r) => r["latitude"] == "%s")
               |> filter(fn: (r) => r["longitude"] == "%s")
            """.formatted(
            influxDbBucket,
            session.getStartingTimestamp(),
            session.getEndingTimestamp(),
            measurementFieldValue,
            latitude,
            longitude
        );

        // Get the Query API
        QueryApi queryApi = influxDBClient.getQueryApi();

        // Execute the query
        List<FluxTable> fluxTables = queryApi.query(query);

        Map<String, TimeSeries> weatherData = new HashMap<>();
        // Build weather data report
        for (FluxTable fluxTable : fluxTables) {
            for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                weatherData.computeIfAbsent(fluxRecord.getField(), k -> new TimeSeries()).addDataPoint(
                    fluxRecord.getTime(),
                    fluxRecord.getValue()
                );
            }
        }
        return new WeatherDataReport(weatherData);
    }
}
