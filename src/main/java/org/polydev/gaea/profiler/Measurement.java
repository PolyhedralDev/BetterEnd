package org.polydev.gaea.profiler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Measurement {
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private final List<Long> measurements;
    public Measurement() {
        measurements = new ArrayList<>();
    }

    public void record(long value) {
        max = Math.max(value, max);
        min = Math.min(value, min);
        measurements.add(value);
    }

    public ProfileFuture beginMeasurement() {
        ProfileFuture future = new ProfileFuture();
        long current = System.nanoTime();
        future.thenRun(() -> record(System.nanoTime()-current));
        return future;
    }

    public void reset() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        measurements.clear();
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public long average() {
        BigInteger running = new BigInteger("0");
        for(Long l : measurements) {
            running = running.add(BigInteger.valueOf(l));
        }
        if(measurements.size() == 0) return 0;
        return running.divide(BigInteger.valueOf(measurements.size())).longValue();
    }

    public int entries() {
        return measurements.size();
    }

}
