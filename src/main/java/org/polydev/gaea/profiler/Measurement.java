package org.polydev.gaea.profiler;

import org.polydev.gaea.math.MathUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Measurement {
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private final List<Long> measurements;
    private final long desirable;
    private final DataType type;
    public Measurement(long desirable, DataType type) {
        this.desirable = desirable;
        this.type = type;
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

    public DataHolder getDataHolder() {
        return new DataHolder(type, desirable, 0.25);
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

    public double getStdDev() {
        double[] vals = new double[measurements.size()];
        for(int i = 0; i < measurements.size(); i++) {
            vals[i] = measurements.get(i);
        }
        return MathUtil.standardDeviation(vals);
    }

    public int entries() {
        return measurements.size();
    }

}
