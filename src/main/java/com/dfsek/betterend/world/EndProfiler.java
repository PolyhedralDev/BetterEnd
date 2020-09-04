package com.dfsek.betterend.world;

import org.bukkit.World;
import org.polydev.gaea.profiler.DataType;
import org.polydev.gaea.profiler.Measurement;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.HashMap;

public class EndProfiler extends WorldProfiler {
    private static final HashMap<World, EndProfiler> profilers = new HashMap<>();
    public EndProfiler(World w) {
        super(w);
        this.addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "TotalChunkGenTime")
                .addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "ChunkBaseGenTime")
                .addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "GenPopulatorTime")
                .addMeasurement(new Measurement(50000, DataType.PERIOD_MILLISECONDS), "BiomeSetTime")
                .addMeasurement(new Measurement(15000000, DataType.PERIOD_MILLISECONDS), "TreeGenTime")
                .addMeasurement(new Measurement(75000000, DataType.PERIOD_MILLISECONDS), "StructureGenTime")
                .addMeasurement(new Measurement(3000000, DataType.PERIOD_MILLISECONDS), "StructureFeatureTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "SnowTime")
                .addMeasurement(new Measurement(1500000, DataType.PERIOD_MILLISECONDS), "FaunaTime");
    }
    public static EndProfiler fromWorld(World w) {
        if(profilers.containsKey(w)) return profilers.get(w);
        EndProfiler p = new EndProfiler(w);
        profilers.put(w, p);
        return p;
    }
}
