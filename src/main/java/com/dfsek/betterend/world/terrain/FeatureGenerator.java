package com.dfsek.betterend.world.terrain;

import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public abstract class FeatureGenerator {
    public abstract ChunkSlice generateSlice(ChunkSlice slice, Random random);
}
