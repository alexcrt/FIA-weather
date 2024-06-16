package org.fia.service.parameter;

import java.time.Duration;
import java.time.LocalDateTime;

public record AddGrandPrixParameter(
    int seasonYear,
    String trackName, String trackLatitude, String trackLongitude, int trackZoneOffsetHours,
    boolean isSprint,
    LocalDateTime fp1StartingDateTime, Duration fp1Duration,
    LocalDateTime fp2StartingDateTime, Duration fp2Duration,
    LocalDateTime fp3StartingDateTime, Duration fp3Duration,
    LocalDateTime sq1StartingDateTime, Duration sq1Duration,
    LocalDateTime sq2StartingDateTime, Duration sq2Duration,
    LocalDateTime sq3StartingDateTime, Duration sq3Duration,
    LocalDateTime sprintRaceStartingDateTime, Duration sprintRaceDuration,
    LocalDateTime q1StartingDateTime, Duration q1Duration,
    LocalDateTime q2StartingDateTime, Duration q2Duration,
    LocalDateTime q3StartingDateTime, Duration q3Duration,
    LocalDateTime raceStartingDateTime, Duration raceDuration
) {
}
