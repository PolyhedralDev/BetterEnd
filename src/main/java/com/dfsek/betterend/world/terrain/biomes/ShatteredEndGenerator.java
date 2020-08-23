package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.TerrainUtil;
import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.WorldConfig;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import com.dfsek.betterend.world.terrain.features.EndCaveGenerator;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public class ShatteredEndGenerator extends BiomeGenerator {
    private final WorldConfig config;
    public ShatteredEndGenerator(World world) {
        super(world);
        config = WorldConfig.fromWorld(super.getWorld());
    }

    @Override
    public int getMaxHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) (config.islandHeightMultiplierTop * (iNoise - config.landPercent) + 64) - getShatteredNoise(x, z);
    }

    @Override
    public int getMinHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) ((-config.islandHeightMultiplierBottom * (iNoise - config.landPercent) + 64) + 1) + getShatteredNoise(x, z);
    }

    private int getShatteredNoise(int x, int z) {
        return (int) (super.getNoiseGenerator().noise((double) (x) / 10, (double) (z) / 10, 0.5D, 0.7D) * 4 * TerrainUtil.getShatteredLevel(x, z, super.getWorld()));
    }

    @Override
    public ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ) {
        ChunkSlice slice = new ChunkSlice(x, z);
        for(int i = getMinHeight((chunkX << 4) + x, (chunkZ << 4) + z); i < getMaxHeight((chunkX << 4) + x, (chunkZ << 4) + z); i++) {
            slice.setBlock(i, Material.END_STONE);
        }

        return slice;
    }
    @Override
    public List<FeatureGenerator> getFeatures() {
        return Arrays.asList(new EndCaveGenerator());
    }
}
