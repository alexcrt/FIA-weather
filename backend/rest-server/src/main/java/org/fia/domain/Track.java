package org.fia.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fia.converter.ZoneOffsetConverter;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public final class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("random_uuid()")
    private UUID uuid;
    private String name;
    private String latitude;
    private String longitude;
    @Convert(converter = ZoneOffsetConverter.class)
    private ZoneOffset zoneOffset;

    public Track(String name, String latitude, String longitude, ZoneOffset zoneOffset) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoneOffset = zoneOffset;
    }
}
