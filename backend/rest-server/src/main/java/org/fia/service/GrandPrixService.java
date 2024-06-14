package org.fia.service;

import org.fia.domain.GrandPrix;
import org.fia.repository.GrandPrixRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GrandPrixService {

    private final GrandPrixRepository grandPrixRepository;

    public GrandPrixService(GrandPrixRepository grandPrixRepository) {
        this.grandPrixRepository = grandPrixRepository;
    }

    public List<GrandPrix> findAllBySeasonId(UUID seasonUuid) {
        return grandPrixRepository.findAllBySeasonId(seasonUuid);
    }

    public Optional<GrandPrix> findById(UUID grandPrixUuid) {
        return grandPrixRepository.findById(grandPrixUuid);
    }
}
