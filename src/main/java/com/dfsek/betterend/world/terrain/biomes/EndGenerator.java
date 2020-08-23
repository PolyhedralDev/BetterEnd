package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.WorldConfig;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import com.dfsek.betterend.world.terrain.features.EndCaveGenerator;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Collections;
import java.util.List;

public class EndGenerator extends BiomeGenerator {
    private final WorldConfig config;
    public EndGenerator(World world) {
        super(world);
        config = WorldConfig.fromWorld(world);
    }

    @Override
    public int getMaxHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) (config.islandHeightMultiplierTop * (iNoise - ConfigUtil.landPercent) + 64);
    }

    @Override
    public int getMinHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / config.outerEndNoise, (double) z / config.outerEndNoise, 0.1D,
                0.55D);
        return (int) ((-config.islandHeightMultiplierBottom * (iNoise - config.landPercent) + 64) + 1);
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
        return Collections.singletonList(new EndCaveGenerator());
    }
}
