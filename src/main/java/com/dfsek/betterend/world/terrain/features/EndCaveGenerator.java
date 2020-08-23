package com.dfsek.betterend.world.terrain.features;

import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;

import java.util.Random;

public class EndCaveGenerator extends FeatureGenerator {
    @Override
    public ChunkSlice generateSlice(ChunkSlice slice, Random random) {
        return slice;
    }
}
