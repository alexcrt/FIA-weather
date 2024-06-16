package org.fia.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.fia.domain.GrandPrix;
import org.fia.domain.TimedSession;
import org.fia.domain.Track;
import org.fia.service.parameter.DagTriggerParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AirflowDagService {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final String OPEN_METEO_HISTORICAL_API_URL = "https://archive-api.open-meteo.com/v1/archive";
    private static final String INFLUX_DB_HISTORICAL_SERIES_KEY = "historical";

    private static final String OPEN_METEO_FORECAST_API_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String INFLUX_DB_FORECAST_SERIES_KEY = "forecast";

    @Value("${airflow.url}")
    private String airflowUrl;
    @Value("${airflow.user}")
    private String airflowUser;
    @Value("${airflow.password}")
    private String airflowPassword;
    @Value("${airflow.weather_etl_dag_id}")
    private String weatherETLDagId;

    public Map<String, Object> listDags() {
        String url = airflowUrl + "/dags";

        Request request = new Request.Builder()
            .url(url)
            .addHeader("Authorization", Credentials.basic(airflowUser, airflowPassword))
            .addHeader("Content-Type", "application/json")
            .get()
            .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(response.message());
            }
            return OBJECT_MAPPER.readValue(response.body().bytes(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> triggerDag(DagTriggerParameter dagTriggerParameter) {
        String openMeteoURL;
        String influxDbSeriesKey;

        if (dagTriggerParameter.requestForecast()) {
            openMeteoURL = OPEN_METEO_FORECAST_API_URL;
            influxDbSeriesKey = INFLUX_DB_FORECAST_SERIES_KEY;
        } else {
            openMeteoURL = OPEN_METEO_HISTORICAL_API_URL;
            influxDbSeriesKey = INFLUX_DB_HISTORICAL_SERIES_KEY;
        }

        return triggerDag(
            openMeteoURL,
            influxDbSeriesKey,
            dagTriggerParameter.latitude(),
            dagTriggerParameter.longitude(),
            dagTriggerParameter.startDate(),
            dagTriggerParameter.endDate()
        );
    }

    public List<Map<String, Object>> triggerDags(GrandPrix grandPrix) {
        List<TimedSession> commonTimedSessionList = List.of(
            grandPrix.getFp1(),
            grandPrix.getQ1(),
            grandPrix.getQ2(),
            grandPrix.getQ3(),
            grandPrix.getRace()
        );
        List<TimedSession> timedSessionList = new ArrayList<>(commonTimedSessionList);
        if (grandPrix.isSprint()) {
            timedSessionList.addAll(List.of(
                grandPrix.getSprintQ1(),
                grandPrix.getSprintQ2(),
                grandPrix.getSprintQ3(),
                grandPrix.getSprintRace()
            ));
        } else {
            timedSessionList.addAll(List.of(
                grandPrix.getFp2(),
                grandPrix.getFp3()
            ));
        }
        List<Map<String, Object>> runs = new ArrayList<>();

        for (TimedSession timedSession : timedSessionList) {
            runs.add(triggerDag(grandPrix, timedSession));
        }
        return runs;
    }

    public Map<String, Object> triggerDag(GrandPrix grandPrix, TimedSession timedSession) {
        LocalDate startDate = Instant.ofEpochSecond(timedSession.getStartingTimestamp()).atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate endDate = Instant.ofEpochSecond(timedSession.getEndingTimestamp()).atZone(ZoneOffset.UTC).toLocalDate();
        Track track = grandPrix.getTrack();

        // From the historical weather API: You can access data dating back to 1940 with a delay of 2 days.
        long twoDaysAgo = LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC);

        if (timedSession.getEndingTimestamp() <= twoDaysAgo) {
            return triggerDag(
                OPEN_METEO_HISTORICAL_API_URL,
                INFLUX_DB_HISTORICAL_SERIES_KEY,
                track.getLatitude(),
                track.getLongitude(),
                startDate,
                endDate
            );
        } else {
            return triggerDag(
                OPEN_METEO_FORECAST_API_URL,
                INFLUX_DB_FORECAST_SERIES_KEY,
                track.getLatitude(),
                track.getLongitude(),
                startDate,
                endDate
            );
        }
    }

    private Map<String, Object> triggerDag(String openMeteoURL, String influxSeriesKey, String latitude, String longitude, LocalDate startDate, LocalDate endDate) {
        String url = airflowUrl + "/dags/" + weatherETLDagId + "/dagRuns";
        String json = String.format(
            """
                {
                    "conf": {
                        "open_meteo_url": "%s",
                        "influxdb_series_key": "%s",
                        "latitude": "%s",
                        "longitude": "%s",
                        "start_date": "%s",
                        "end_date": "%s"
                    }
                }
                """,
            openMeteoURL,
            influxSeriesKey,
            latitude,
            longitude,
            DATE_TIME_FORMATTER.format(startDate),
            DATE_TIME_FORMATTER.format(endDate)
        );

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", Credentials.basic(airflowUser, airflowPassword))
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(response.message());
            }
            return OBJECT_MAPPER.readValue(response.body().bytes(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
