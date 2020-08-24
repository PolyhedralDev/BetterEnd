package com.dfsek.betterend.world.generation.features.generators;

import com.dfsek.betterend.world.generation.ChunkSlice;
import com.dfsek.betterend.world.generation.features.FeatureGenerator;
import org.bukkit.World;

import java.util.Random;

public class CloudGenerator extends FeatureGenerator {
    @Override
    public ChunkSlice generateSlice(ChunkSlice slice, Random random, World world) {
        
        return slice;
    }
}
