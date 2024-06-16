package org.fia.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.ZoneOffset;

@Converter(autoApply = true)
public class ZoneOffsetConverter implements AttributeConverter<ZoneOffset, String> {
    @Override
    public String convertToDatabaseColumn(ZoneOffset zoneOffset) {
        return zoneOffset != null ? zoneOffset.toString() : null;
    }

    @Override
    public ZoneOffset convertToEntityAttribute(String dbData) {
        return dbData != null ? ZoneOffset.of(dbData) : null;
    }
}
