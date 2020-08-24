package com.dfsek.betterend.world.generation.biomes;

import com.dfsek.betterend.world.generation.ChunkSlice;
import com.dfsek.betterend.world.generation.features.FeatureGenerator;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;
import java.util.Random;

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
    public ChunkSlice getSlice(byte x, byte z, int chunkX, int chunkZ) {
        ChunkSlice slice = this.generateSlice(x, z, chunkX, chunkZ);
        for(FeatureGenerator g : this.getFeatures()) {
            g.generateSlice(slice, new Random(this.getFeatureRandomSeed(x, z, chunkX, chunkZ)), this.getWorld());
        }
        return slice;
    }
    private long getFeatureRandomSeed(byte x, byte z, int chunkX, int chunkZ) {
        return this.getWorld().getSeed() + (chunkX << 4) + x * ((chunkZ << 4) + z);
    }
    public abstract int getMaxHeight(int x, int z);
    public abstract int getMinHeight(int x, int z);
    public abstract ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ);
    public abstract List<FeatureGenerator> getFeatures();
}
