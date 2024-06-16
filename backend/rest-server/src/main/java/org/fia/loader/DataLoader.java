package org.fia.loader;

import org.fia.domain.Season;
import org.fia.domain.Track;
import org.fia.repository.GrandPrixRepository;
import org.fia.repository.RaceRepository;
import org.fia.repository.SeasonRepository;
import org.fia.repository.TrackRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class DataLoader implements ApplicationRunner {

    @Inject
    private TrackRepository trackRepository;

    @Inject
    private SeasonRepository seasonRepository;

    @Inject
    private RaceRepository raceRepository;

    @Inject
    private GrandPrixRepository grandPrixRepository;

    public void run(ApplicationArguments args) {
        Map<Integer, Season> seasons = createSeasons();
        Map<String, Track> tracks = createTracks();

        seasonRepository.saveAll(seasons.values());
        trackRepository.saveAll(tracks.values());
        grandPrixRepository.saveAll(Season2024.all(seasons.get(2024), tracks));
    }

    public Map<Integer, Season> createSeasons() {
        return IntStream.range(2024, 2025)
            .mapToObj(Season::new)
            .collect(Collectors.toMap(Season::getStartingYear, season -> season));
    }

    public Map<String, Track> createTracks() {
        return Stream.of(
            new Track("Monaco", "43.738347784533", "7.424450755119324", ZoneOffset.ofHours(1)),
            new Track("Monza", "45.5845001", "9.2744485", ZoneOffset.ofHours(1)),
            new Track("Canada", "45.501648", "-73.528145", ZoneOffset.ofHours(-4))
        ).collect(Collectors.toMap(Track::getName, track -> track));
    }
}
