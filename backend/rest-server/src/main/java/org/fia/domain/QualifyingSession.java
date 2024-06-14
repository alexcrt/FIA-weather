package org.fia.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public final class QualifyingSession extends TimedSession {
    public QualifyingSession(long startingTimestamp, long endingTimestamp) {
        super(startingTimestamp, endingTimestamp);
    }
}
