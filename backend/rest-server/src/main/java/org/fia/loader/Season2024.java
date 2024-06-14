package org.fia.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.fia.domain.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Season2024 {
    public static List<GrandPrix> all(Season season, Map<String, Track> tracks) {
        if (season.getStartingYear() != 2024) {
            throw new IllegalArgumentException();
        }
        return List.of(
            canada(season, tracks.get("Canada")),
            monza(season, tracks.get("Monza"))
        );
    }

    private static GrandPrix canada(Season season, Track track) {
        ZoneId zoneId = ZoneId.of("UTC-4");
        return GrandPrix.createClassicGrandPrix(
            season,
            track,
            // FP1: 7th June -> 13:30 - 14:30
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 7, 13, 30, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 7, 14, 30, zoneId)
            ),
            // FP2: 7th June -> 17:00 - 18:00
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 7, 17, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 7, 18, 0, zoneId)
            ),
            // FP3: 8th June -> 12:30 - 13:30
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 12, 30, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 13, 30, zoneId)
            ),
            // Q1: 8th June -> 16:00 - 16:18
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 16, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 16, 18, zoneId)
            ),
            // Q2: 8th June -> 16:25 - 16:40
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 16, 25, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 16, 40, zoneId)
            ),
            // Q3: 8th June -> 16:47 - 17:00
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 16, 47, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 8, 17, 0, zoneId)
            ),
            // Race: 9th June -> 14:00 - 16:00
            new Race(
                DateTimeUtils.getEpochSeconds(2024, 6, 9, 14, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 9, 16, 0, zoneId)
            )
        );
    }

    private static GrandPrix monza(Season season, Track track) {
        ZoneId zoneId = ZoneId.of("UTC-4");
        return GrandPrix.createClassicGrandPrix(
            season,
            track,
            // FP1: 7th June -> 13:30 - 14:30
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 15, 13, 30, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 15, 14, 30, zoneId)
            ),
            // FP2: 7th June -> 17:00 - 18:00
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 15, 17, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 15, 18, 0, zoneId)
            ),
            // FP3: 8th June -> 12:30 - 13:30
            new FreePracticeSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 16, 12, 30, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 16, 13, 30, zoneId)
            ),
            // Q1: 8th June -> 16:00 - 16:18
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 16, 16, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 16, 16, 18, zoneId)
            ),
            // Q2: 8th June -> 16:25 - 16:40
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 17, 16, 25, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 17, 16, 40, zoneId)
            ),
            // Q3: 8th June -> 16:47 - 17:00
            new QualifyingSession(
                DateTimeUtils.getEpochSeconds(2024, 6, 17, 16, 47, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 17, 17, 0, zoneId)
            ),
            // Race: 9th June -> 14:00 - 16:00
            new Race(
                DateTimeUtils.getEpochSeconds(2024, 6, 18, 14, 0, zoneId),
                DateTimeUtils.getEpochSeconds(2024, 6, 18, 16, 0, zoneId)
            )
        );
    }
}
