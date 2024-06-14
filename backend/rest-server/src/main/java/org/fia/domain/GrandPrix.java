package org.fia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.UUID;

@Entity
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
}
