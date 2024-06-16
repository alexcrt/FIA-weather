package org.fia.service;

import org.fia.domain.*;
import org.fia.repository.GrandPrixRepository;
import org.fia.repository.SeasonRepository;
import org.fia.repository.TrackRepository;
import org.fia.service.parameter.AddGrandPrixParameter;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.fia.utils.DateTimeUtils.utcEpochSeconds;

@Service
public class GrandPrixService {

    private final GrandPrixRepository grandPrixRepository;
    private final TrackRepository trackRepository;
    private final SeasonRepository seasonRepository;

    public GrandPrixService(GrandPrixRepository grandPrixRepository, TrackRepository trackRepository, SeasonRepository seasonRepository) {
        this.grandPrixRepository = grandPrixRepository;
        this.trackRepository = trackRepository;
        this.seasonRepository = seasonRepository;
    }

    public List<GrandPrix> findAllBySeasonId(UUID seasonUuid) {
        return grandPrixRepository.findAllBySeasonId(seasonUuid);
    }

    public Optional<GrandPrix> findById(UUID grandPrixUuid) {
        return grandPrixRepository.findById(grandPrixUuid);
    }

    public void addGrandPrix(AddGrandPrixParameter addGrandPrixParameter) {
        int seasonYear = addGrandPrixParameter.seasonYear();
        Season season = seasonRepository.findByYear(seasonYear).orElseGet(() -> new Season(seasonYear));

        String trackName = addGrandPrixParameter.trackName();
        Track track = trackRepository.findByName(trackName).orElseGet(() -> new Track(
            trackName,
            addGrandPrixParameter.trackLatitude(),
            addGrandPrixParameter.trackLongitude(),
            ZoneOffset.ofHours(addGrandPrixParameter.trackZoneOffsetHours())
        ));

        seasonRepository.save(season);
        trackRepository.save(track);

        ZoneOffset trackZoneOffset = track.getZoneOffset();

        GrandPrix grandPrix;
        if (addGrandPrixParameter.isSprint()) {
            grandPrix = GrandPrix.createSprintGrandPrix(
                season,
                track,
                new FreePracticeSession(
                    utcEpochSeconds(addGrandPrixParameter.fp1StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.fp1StartingDateTime().plus(addGrandPrixParameter.fp1Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.sq1StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.sq1StartingDateTime().plus(addGrandPrixParameter.sq1Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.sq2StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.sq2StartingDateTime().plus(addGrandPrixParameter.sq2Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.sq3StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.sq3StartingDateTime().plus(addGrandPrixParameter.sq3Duration()), trackZoneOffset)
                ),
                new Race(
                    utcEpochSeconds(addGrandPrixParameter.sprintRaceStartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.sprintRaceStartingDateTime().plus(addGrandPrixParameter.sprintRaceDuration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q1StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q1StartingDateTime().plus(addGrandPrixParameter.q1Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q2StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q2StartingDateTime().plus(addGrandPrixParameter.q2Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q3StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q3StartingDateTime().plus(addGrandPrixParameter.q3Duration()), trackZoneOffset)
                ),
                new Race(
                    utcEpochSeconds(addGrandPrixParameter.raceStartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.raceStartingDateTime().plus(addGrandPrixParameter.raceDuration()), trackZoneOffset)
                )
            );
        } else {
            grandPrix = GrandPrix.createClassicGrandPrix(
                season,
                track,
                new FreePracticeSession(
                    utcEpochSeconds(addGrandPrixParameter.fp1StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.fp1StartingDateTime().plus(addGrandPrixParameter.fp1Duration()), trackZoneOffset)
                ),
                new FreePracticeSession(
                    utcEpochSeconds(addGrandPrixParameter.fp2StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.fp2StartingDateTime().plus(addGrandPrixParameter.fp2Duration()), trackZoneOffset)
                ),
                new FreePracticeSession(
                    utcEpochSeconds(addGrandPrixParameter.fp3StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.fp3StartingDateTime().plus(addGrandPrixParameter.fp3Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q1StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q1StartingDateTime().plus(addGrandPrixParameter.q1Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q2StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q2StartingDateTime().plus(addGrandPrixParameter.q2Duration()), trackZoneOffset)
                ),
                new QualifyingSession(
                    utcEpochSeconds(addGrandPrixParameter.q3StartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.q3StartingDateTime().plus(addGrandPrixParameter.q3Duration()), trackZoneOffset)
                ),
                new Race(
                    utcEpochSeconds(addGrandPrixParameter.raceStartingDateTime(), trackZoneOffset),
                    utcEpochSeconds(addGrandPrixParameter.raceStartingDateTime().plus(addGrandPrixParameter.raceDuration()), trackZoneOffset)
                )
            );
        }
        grandPrixRepository.save(grandPrix);
    }
}
