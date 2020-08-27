package com.dfsek.betterend.generation.features;

import com.dfsek.betterend.generation.ChunkSlice;
import org.bukkit.World;

import java.util.Random;

public abstract class FeatureGenerator {
    public abstract ChunkSlice generateSlice(ChunkSlice slice, Random random, World world);
}
