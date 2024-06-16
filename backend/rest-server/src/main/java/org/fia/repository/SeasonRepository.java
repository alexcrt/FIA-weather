package org.fia.repository;

import org.fia.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonRepository extends JpaRepository<Season, UUID> {
    @Query("SELECT s FROM Season s WHERE s.startingYear = :year")
    Optional<Season> findByYear(int year);
}
