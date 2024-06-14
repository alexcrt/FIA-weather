package org.fia.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class TimedSession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("random_uuid()")
    @Getter
    private UUID uuid;
    private long startingTimestamp;
    private long endingTimestamp;

    public TimedSession(long startingTimestamp, long endingTimestamp) {
        this.startingTimestamp = startingTimestamp;
        this.endingTimestamp = endingTimestamp;
    }
}
