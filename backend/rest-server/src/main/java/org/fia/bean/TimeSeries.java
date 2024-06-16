package org.fia.bean;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TimeSeries {
    private final List<Instant> timestamps;
    private final List<Object> values;

    public TimeSeries() {
        this.timestamps = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public void addDataPoint(Instant instant, Object value) {
        this.timestamps.add(instant);
        this.values.add(value);
    }
}
