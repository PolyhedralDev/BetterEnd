package com.dfsek.betterend.world.terrain;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;

public abstract class BiomeGenerator {
    private final SimplexOctaveGenerator generator;
    private final World world;
    public BiomeGenerator(World world) {
        this.generator = new SimplexOctaveGenerator(world.getSeed(), 4);
        this.world = world;
    }
    public SimplexOctaveGenerator getNoiseGenerator() {
        return generator;
    }
    public World getWorld() {
        return world;
    }
    public abstract int getMaxHeight(int x, int z);
    public abstract int getMinHeight(int x, int z);
    public abstract ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ);
    public abstract List<FeatureGenerator> getFeatures();
}
