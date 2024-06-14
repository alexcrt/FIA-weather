package org.fia.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {
    public static long getEpochSeconds(int year, int month, int day, int hour, int minute, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, 0, 0, zoneId);
        return zonedDateTime.toEpochSecond();
    }

}
