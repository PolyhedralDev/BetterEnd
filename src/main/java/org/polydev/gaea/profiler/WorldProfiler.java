package org.polydev.gaea.profiler;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class WorldProfiler {
    private boolean isProfiling;
    private final Map<String, Measurement> measures = new HashMap<>();
    private final World world;

    public WorldProfiler(World w) {
        isProfiling = false;
        this.world = w;
    }

    public String getResultsFormatted() {
        if(!isProfiling) return "Profiler is not currently running.";
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String, Measurement> e : measures.entrySet()) {
            result.append(e.getKey())
                    .append(": Avg ")
                    .append((double) e.getValue().average()/1000000)
                    .append("ms, Min")
                    .append((double) e.getValue().getMin()/1000000)
                    .append("ms, Max ")
                    .append((double) e.getValue().getMax()/1000000)
                    .append("\n");
        }
        return result.toString();
    }

    public void reset() {
        for(Map.Entry<String, Measurement> e : measures.entrySet()) {
            e.getValue().reset();
        }
    }

    public WorldProfiler addMeasurement(Measurement m, String name) {
        measures.put(name, m);
        return this;
    }

    public boolean setMeasurement(String id, long value) {
        if(isProfiling) measures.get(id).record(value);
        return isProfiling;
    }

    public ProfileFuture measure(String id) {
        if(isProfiling) return measures.get(id).beginMeasurement();
        else return null;
    }

    public void setProfiling(boolean enabled) {
        this.isProfiling = enabled;
    }

    public World getWorld() {
        return world;
    }
}
