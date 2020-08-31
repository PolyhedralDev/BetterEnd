package com.dfsek.betterend.generation.features;

import org.polydev.gaea.world.ChunkSlice;
import org.bukkit.World;

import java.util.Random;

public abstract class FeatureGenerator {
    public abstract ChunkSlice generateSlice(ChunkSlice slice, Random random, World world);
}
