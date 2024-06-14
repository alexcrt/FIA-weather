package org.fia.repository;

import org.fia.domain.GrandPrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GrandPrixRepository extends JpaRepository<GrandPrix, UUID> {
    @Query("SELECT gp FROM GrandPrix gp JOIN gp.race r WHERE gp.season.uuid = :seasonUuid ORDER BY r.startingTimestamp ASC")
    List<GrandPrix> findAllBySeasonId(UUID seasonUuid);
}
