package com.dfsek.betterend.world.terrain;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;

public abstract class BiomeGenerator {
    private final SimplexOctaveGenerator generator;
    public BiomeGenerator(World world) {
        generator = new SimplexOctaveGenerator(world.getSeed(), 4);
    }
    public SimplexOctaveGenerator getNoiseGenerator() {
        return generator;
    }
    public abstract int getMaxHeight(int x, int z);
    public abstract int getMinHeight(int x, int z);
    public abstract ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ);
    public abstract List<FeatureGenerator> getFeatures();
}
