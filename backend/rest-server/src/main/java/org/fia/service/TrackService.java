package org.fia.service;

import org.fia.domain.Track;
import org.fia.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll().stream().sorted(Comparator.comparing(Track::getName)).toList();
    }
}
