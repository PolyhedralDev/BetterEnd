package com.dfsek.betterend.world.terrain;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;

public interface BiomeGenerator {
    int getMaxHeight(int x, int z, long seed);
    int getMinHeight(int x, int z, long seed);
    ChunkSlice generateSlice(byte x, byte z, Chunk chunk);
    List<FeatureGenerator> getFeatures();
}
