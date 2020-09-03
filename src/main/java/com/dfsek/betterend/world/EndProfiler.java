package com.dfsek.betterend.world;

import org.bukkit.World;
import org.polydev.gaea.profiler.Measurement;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.HashMap;

public class EndProfiler extends WorldProfiler {
    private static final HashMap<World, EndProfiler> profilers = new HashMap<>();
    public EndProfiler(World w) {
        super(w);
        this.addMeasurement(new Measurement(), "TotalChunkGenTime")
                .addMeasurement(new Measurement(), "BiomeSetTime")
                .addMeasurement(new Measurement(), "TreeGenTime")
                .addMeasurement(new Measurement(), "StructureGenTime");
    }
    public static EndProfiler fromWorld(World w) {
        if(profilers.containsKey(w)) return profilers.get(w);
        EndProfiler p = new EndProfiler(w);
        profilers.put(w, p);
        return p;
    }
}
