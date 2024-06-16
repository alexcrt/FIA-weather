package org.fia.repository;

import org.fia.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {
    @Query("SELECT t FROM Track t WHERE t.name = :name")
    Optional<Track> findByName(String name);
}
