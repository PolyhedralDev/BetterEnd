package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;

public class AetherGenerator implements BiomeGenerator {
    @Override
    public int getMaxHeight(int x, int z, long seed) {
        return 0;
    }

    @Override
    public int getMinHeight(int x, int z, long seed) {
        return 0;
    }

    @Override
    public ChunkSlice generateSlice(byte x, byte z, Chunk chunk) {
        return null;
    }

    @Override
    public List<FeatureGenerator> getFeatures() {
        return null;
    }
}
