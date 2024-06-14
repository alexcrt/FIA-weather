package org.fia.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class FreePracticeSession extends TimedSession {
    public FreePracticeSession(long startingTimestamp, long endingTimestamp) {
        super(startingTimestamp, endingTimestamp);
    }
}
