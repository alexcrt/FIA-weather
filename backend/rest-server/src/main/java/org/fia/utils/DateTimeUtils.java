package org.fia.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {
    public static long epochSeconds(int year, int month, int day, int hour, int minute, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, 0, 0, zoneId);
        return zonedDateTime.toEpochSecond();
    }

    public static long utcEpochSeconds(LocalDateTime localDateTime) {
        return utcEpochSeconds(localDateTime, ZoneOffset.UTC);
    }

    public static long utcEpochSeconds(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return localDateTime.toInstant(zoneOffset).getEpochSecond();
    }
}
