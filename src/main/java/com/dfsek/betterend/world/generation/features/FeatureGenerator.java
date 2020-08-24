package com.dfsek.betterend.world.generation.features;

import com.dfsek.betterend.world.generation.ChunkSlice;
import org.bukkit.World;

import java.util.Random;

public abstract class FeatureGenerator {
    public abstract ChunkSlice generateSlice(ChunkSlice slice, Random random, World world);
}
