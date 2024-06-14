package org.fia.repository;

import org.fia.domain.QualifyingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QualifyingSessionRepository extends JpaRepository<QualifyingSession, UUID> {
}
