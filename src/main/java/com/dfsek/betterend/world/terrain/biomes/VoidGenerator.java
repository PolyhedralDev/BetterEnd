package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.TerrainUtil;
import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.WorldConfig;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class VoidGenerator extends BiomeGenerator {
    private final WorldConfig config;
    public VoidGenerator(World world) {
        super(world);
        config = WorldConfig.fromWorld(world);
    }

    @Override
    public int getMaxHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) (config.islandHeightMultiplierTop * (iNoise*(1D- TerrainUtil.getVoidLevel(x, z, super.getWorld())) - config.landPercent) + 64);
    }

    @Override
    public int getMinHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) ((-config.islandHeightMultiplierBottom * (iNoise*(1D- TerrainUtil.getVoidLevel(x, z, super.getWorld())) - config.landPercent) + 64) + 1);
    }

    @Override
    public ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ) {
        ChunkSlice slice = new ChunkSlice(x, z);
        int min = getMinHeight((chunkX << 4) + x, (chunkZ << 4) + z);
        int max = getMaxHeight((chunkX << 4) + x, (chunkZ << 4) + z);
        if(TerrainUtil.isAetherVoid((chunkX << 4) + x, (chunkZ << 4) + z, super.getWorld())) {
            for(int i = min; i < max; i++) {
                if(i == max-1) slice.setBlock(i, Material.GRASS_BLOCK);
                else if(i > max-3) slice.setBlock(i, Material.DIRT);
                else slice.setBlock(i, Material.STONE);
            }
        } else {
            for(int i = min; i < max; i++) {
                slice.setBlock(i, Material.END_STONE);
            }
        }
        return slice;
    }
    @Override
    public List<FeatureGenerator> getFeatures() {
        return null;
    }
}
