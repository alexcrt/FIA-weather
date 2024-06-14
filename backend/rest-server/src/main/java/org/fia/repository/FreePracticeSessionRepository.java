package org.fia.repository;

import org.fia.domain.FreePracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FreePracticeSessionRepository extends JpaRepository<FreePracticeSession, UUID> {
}
