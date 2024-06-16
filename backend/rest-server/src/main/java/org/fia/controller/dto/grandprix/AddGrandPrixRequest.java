package org.fia.controller.dto.grandprix;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddGrandPrixRequest {
    @NotBlank
    private String trackName;
    @NotBlank
    private String trackLatitude;
    @NotBlank
    private String trackLongitude;

    private int trackZoneOffsetHours;
    @JsonProperty("isSprint")
    private boolean isSprint;
    @NotBlank
    private LocalDateTime fp1StartingDateTime;
    @Positive
    private long fp1Duration;
    private LocalDateTime fp2StartingDateTime;
    private Long fp2Duration;
    private LocalDateTime fp3StartingDateTime;
    private Long fp3Duration;
    private LocalDateTime sq1StartingDateTime;
    private Long sq1Duration;
    private LocalDateTime sq2StartingDateTime;
    private Long sq2Duration;
    private LocalDateTime sq3StartingDateTime;
    private Long sq3Duration;
    private LocalDateTime sprintRaceStartingDateTime;
    private Long sprintRaceDuration;
    @NotBlank
    private LocalDateTime q1StartingDateTime;
    @Positive
    private long q1Duration;
    @NotBlank
    private LocalDateTime q2StartingDateTime;
    @Positive
    private long q2Duration;
    @NotBlank
    private LocalDateTime q3StartingDateTime;
    @Positive
    private long q3Duration;
    @NotBlank
    private LocalDateTime raceStartingDateTime;
    @Positive
    private long raceDuration;
}
