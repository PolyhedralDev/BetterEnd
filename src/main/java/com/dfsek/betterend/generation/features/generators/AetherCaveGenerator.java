package com.dfsek.betterend.generation.features.generators;

import com.dfsek.betterend.generation.ChunkSlice;
import com.dfsek.betterend.generation.features.FeatureGenerator;
import org.bukkit.World;

import java.util.Random;

public class AetherCaveGenerator extends FeatureGenerator {
    @Override
    public ChunkSlice generateSlice(ChunkSlice slice, Random random, World world) {
        return slice;
    }
}