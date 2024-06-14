package org.fia.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public final class Race extends TimedSession {
    public Race(long startingTimestamp, long endingTimestamp) {
        super(startingTimestamp, endingTimestamp);
    }
}
