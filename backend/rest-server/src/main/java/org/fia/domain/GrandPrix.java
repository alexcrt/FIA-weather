package org.fia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"track_id", "season_id"})
    }
)
@Getter
@Builder
@AllArgsConstructor
public class GrandPrix implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("random_uuid()")
    private UUID uuid;
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    private boolean isSprint;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fp1_id")
    private FreePracticeSession fp1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fp2_id")
    private FreePracticeSession fp2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fp3_id")
    private FreePracticeSession fp3;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sq1_id")
    private QualifyingSession sprintQ1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sq2_id")
    private QualifyingSession sprintQ2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sq3_id")
    private QualifyingSession sprintQ3;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sprint_race_id")
    private Race sprintRace;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "q1_id")
    private QualifyingSession q1;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "q2_id")
    private QualifyingSession q2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "q3_id")
    private QualifyingSession q3;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "race_id")
    private Race race;

    public GrandPrix() {
    }

    public static GrandPrix createSprintGrandPrix(
        Season season,
        Track track,
        FreePracticeSession fp1,
        QualifyingSession sq1,
        QualifyingSession sq2,
        QualifyingSession sq3,
        Race sprintRace,
        QualifyingSession q1,
        QualifyingSession q2,
        QualifyingSession q3,
        Race race
    ) {
        return new GrandPrixBuilder()
            .isSprint(true)
            .season(season)
            .track(track)
            .fp1(fp1)
            .sprintQ1(sq1)
            .sprintQ2(sq2)
            .sprintQ3(sq3)
            .sprintRace(sprintRace)
            .q1(q1)
            .q2(q2)
            .q3(q3)
            .race(race)
            .build();
    }

    public static GrandPrix createClassicGrandPrix(
        Season season,
        Track track,
        FreePracticeSession fp1,
        FreePracticeSession fp2,
        FreePracticeSession fp3,
        QualifyingSession q1,
        QualifyingSession q2,
        QualifyingSession q3,
        Race race
    ) {
        return new GrandPrixBuilder()
            .isSprint(false)
            .season(season)
            .track(track)
            .fp1(fp1)
            .fp2(fp2)
            .fp3(fp3)
            .q1(q1)
            .q2(q2)
            .q3(q3)
            .race(race)
            .build();
    }

    public List<Pair<String, TimedSession>> timedSessionsInOrder() {
        // Help type inference
        return Stream.<Pair<String, TimedSession>>of(
                Pair.of("FP1", this.fp1), Pair.of("FP2", this.fp2), Pair.of("FP3", this.fp3),
                Pair.of("SQ1", this.sprintQ1), Pair.of("SQ2", this.sprintQ2), Pair.of("SQ3", this.sprintQ3), Pair.of("SPRINT_RACE", this.sprintRace),
                Pair.of("Q1", this.q1), Pair.of("Q2", this.q2), Pair.of("Q3", this.q3), Pair.of("RACE", this.race)
            )
            .filter(p -> p.getValue() != null)
            .sorted(Comparator.comparingLong(p -> p.getValue().getStartingTimestamp()))
            .toList();
    }
}
