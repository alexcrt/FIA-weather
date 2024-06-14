package org.fia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Season implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("random_uuid()")
    private UUID uuid;

    private int startingYear;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<GrandPrix> grandPrixList;

    public Season(int startingYear) {
        this.startingYear = startingYear;
    }
}
