package org.fia.loader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.fia.domain.*;
import org.fia.repository.GrandPrixRepository;
import org.fia.repository.RaceRepository;
import org.fia.repository.SeasonRepository;
import org.fia.repository.TrackRepository;
import org.fia.utils.DateTimeUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.fia.domain.GrandPrix.createClassicGrandPrix;
import static org.fia.domain.GrandPrix.createSprintGrandPrix;

@Component
public class DataLoader implements ApplicationRunner {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Inject
    private TrackRepository trackRepository;

    @Inject
    private SeasonRepository seasonRepository;

    @Inject
    private RaceRepository raceRepository;

    @Inject
    private GrandPrixRepository grandPrixRepository;

    public void run(ApplicationArguments args) throws IOException {
        Map<Integer, Season> seasons = createSeasons();
        Map<String, Track> tracks = createTracks();

        seasonRepository.saveAll(seasons.values());
        trackRepository.saveAll(tracks.values());

        List<GrandPrix> grandPrixList = parseCSVAndCreateGrandPrix("/data/season_2024.csv", tracks, seasons.get(2024));
        grandPrixRepository.saveAll(grandPrixList);
    }

    public Map<Integer, Season> createSeasons() {
        return IntStream.range(2024, 2025)
            .mapToObj(Season::new)
            .collect(Collectors.toMap(Season::getStartingYear, season -> season));
    }

    public Map<String, Track> createTracks() {
        return Stream.of(
            new Track("Bahrain International Circuit", "26.0325", "50.5106", ZoneOffset.ofHours(3)),
            new Track("Jeddah Street Circuit", "21.5433", "39.1728", ZoneOffset.ofHours(3)),
            new Track("Albert Park Circuit", "-37.8497", "144.9680", ZoneOffset.ofHours(10)),
            new Track("Shanghai International Circuit", "31.3389", "121.2234", ZoneOffset.ofHours(8)),
            new Track("Miami International Autodrome", "25.9580", "-80.2389", ZoneOffset.ofHours(-4)),
            new Track("Imola", "44.3439", "11.7193", ZoneOffset.ofHours(2)),
            new Track("Circuit de Monaco", "43.7347", "7.4206", ZoneOffset.ofHours(2)),
            new Track("Circuit de Barcelona-Catalunya", "41.5700", "2.2611", ZoneOffset.ofHours(2)),
            new Track("Circuit Gilles Villeneuve", "45.5048", "-73.5262", ZoneOffset.ofHours(-4)),
            new Track("Red Bull Ring", "47.2197", "14.7647", ZoneOffset.ofHours(2)),
            new Track("Silverstone Circuit", "52.0733", "-1.0142", ZoneOffset.UTC),
            new Track("Hungaroring", "47.5789", "19.2486", ZoneOffset.ofHours(2)),
            new Track("Spa-Francorchamps", "50.4372", "5.9714", ZoneOffset.ofHours(2)),
            new Track("Circuit Zandvoort", "52.3888", "4.5409", ZoneOffset.ofHours(2)),
            new Track("Monza", "45.6205", "9.2811", ZoneOffset.ofHours(2)),
            new Track("Marina Bay Street Circuit", "1.2914", "103.8635", ZoneOffset.ofHours(8)),
            new Track("Suzuka International Racing Course", "34.8431", "136.5411", ZoneOffset.ofHours(9)),
            new Track("Losail International Circuit", "25.4848", "51.4542", ZoneOffset.ofHours(3)),
            new Track("Circuit of the Americas", "30.1328", "-97.6401", ZoneOffset.ofHours(-5)),
            new Track("Autódromo Hermanos Rodríguez", "19.4042", "-99.0907", ZoneOffset.ofHours(-6)),
            new Track("Interlagos (Autódromo José Carlos Pace)", "-23.7010", "-46.6972", ZoneOffset.ofHours(-3)),
            new Track("Las Vegas Street Circuit", "36.1147", "-115.1728", ZoneOffset.ofHours(-7)),
            new Track("Yas Marina Circuit", "24.4672", "54.6031", ZoneOffset.ofHours(4))
        ).collect(Collectors.toMap(Track::getName, track -> track));
    }

    private List<GrandPrix> parseCSVAndCreateGrandPrix(String resourcePath, Map<String, Track> tracks, Season season) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();
        List<GrandPrix> grandPrixList = new ArrayList<>();
        try (
            InputStream is = DataLoader.class.getResourceAsStream(resourcePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            CSVParser parser = new CSVParser(reader, csvFormat)
        ) {
            for (CSVRecord record : parser) {
                boolean isSprint = record.get("is_sprint").equals("1");
                Track track = tracks.get(record.get("track_name"));

                FreePracticeSession fp1 = createFreePracticeSession(record, "fp1_start_datetime", "fp1_duration", track.getZoneOffset());

                QualifyingSession q1 = createQualifyingSession(record, "q1_start_datetime", "q1_duration", track.getZoneOffset());
                QualifyingSession q2 = createQualifyingSession(record, "q2_start_datetime", "q2_duration", track.getZoneOffset());
                QualifyingSession q3 = createQualifyingSession(record, "q3_start_datetime", "q3_duration", track.getZoneOffset());

                Race race = createRace(record, "race_start_datetime", "race_duration", track.getZoneOffset());

                GrandPrix grandPrix;
                if (isSprint) {
                    QualifyingSession sq1 = createQualifyingSession(record, "sq1_start_datetime", "sq1_duration", track.getZoneOffset());
                    QualifyingSession sq2 = createQualifyingSession(record, "sq2_start_datetime", "sq2_duration", track.getZoneOffset());
                    QualifyingSession sq3 = createQualifyingSession(record, "sq3_start_datetime", "sq3_duration", track.getZoneOffset());

                    Race sprintRace = createRace(record, "sprint_race_start_datetime", "sprint_race_duration", track.getZoneOffset());
                    grandPrix = createSprintGrandPrix(season, track, fp1, sq1, sq2, sq3, sprintRace, q1, q2, q3, race);
                } else {
                    FreePracticeSession fp2 = createFreePracticeSession(record, "fp2_start_datetime", "fp2_duration", track.getZoneOffset());
                    FreePracticeSession fp3 = createFreePracticeSession(record, "fp3_start_datetime", "fp3_duration", track.getZoneOffset());
                    grandPrix = createClassicGrandPrix(season, track, fp1, fp2, fp3, q1, q2, q3, race);
                }
                grandPrixList.add(grandPrix);
            }
            return grandPrixList;
        }
    }

    private FreePracticeSession createFreePracticeSession(CSVRecord record, String startDateTimeField, String durationField, ZoneOffset zoneOffset) {
        LocalDateTime startDateTime = LocalDateTime.parse(record.get(startDateTimeField), DATE_TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(record.get(durationField)));
        long startingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime, zoneOffset);
        long endingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime.plus(duration), zoneOffset);
        return new FreePracticeSession(startingTimestamp, endingTimestamp);
    }

    private QualifyingSession createQualifyingSession(CSVRecord record, String startDateTimeField, String durationField, ZoneOffset zoneOffset) {
        LocalDateTime startDateTime = LocalDateTime.parse(record.get(startDateTimeField), DATE_TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(record.get(durationField)));
        long startingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime, zoneOffset);
        long endingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime.plus(duration), zoneOffset);
        return new QualifyingSession(startingTimestamp, endingTimestamp);
    }

    private Race createRace(CSVRecord record, String startDateTimeField, String durationField, ZoneOffset zoneOffset) {
        LocalDateTime startDateTime = LocalDateTime.parse(record.get(startDateTimeField), DATE_TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(record.get(durationField)));
        long startingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime, zoneOffset);
        long endingTimestamp = DateTimeUtils.utcEpochSeconds(startDateTime.plus(duration), zoneOffset);
        return new Race(startingTimestamp, endingTimestamp);
    }
}
