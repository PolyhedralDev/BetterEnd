package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import com.dfsek.betterend.world.terrain.features.AetherCaveGenerator;
import com.dfsek.betterend.world.terrain.features.CloudGenerator;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public class AetherHighlandsForestGenerator extends BiomeGenerator {

    public AetherHighlandsForestGenerator(World world) {
        super(world);
    }

    @Override
    public int getMaxHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / ConfigUtil.outerEndNoise, (double) z / ConfigUtil.outerEndNoise, 0.1D,
                0.55D);
        return (int) (ConfigUtil.islandHeightMultiplierTop * (iNoise - ConfigUtil.landPercent) + 64);
    }

    @Override
    public int getMinHeight(int x, int z) {
        double iNoise = super.getNoiseGenerator().noise((double) x / ConfigUtil.outerEndNoise, (double) z / ConfigUtil.outerEndNoise, 0.1D,
                0.55D);
        return (int) ((-ConfigUtil.islandHeightMultiplierBottom * (iNoise - ConfigUtil.landPercent) + 64) + 1);
    }

    @Override
    public ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ) {
        ChunkSlice slice = new ChunkSlice(x, z);
        int max = getMaxHeight((chunkX << 4) + x, (chunkZ << 4) + z);
        int min = getMinHeight((chunkX << 4) + x, (chunkZ << 4) + z);
        for(int i = min; i < max; i++) {
            if(i == max-1) slice.setBlock(i, Material.GRASS_BLOCK);
            else if(i > max-3) slice.setBlock(i, Material.DIRT);
            else slice.setBlock(i, Material.STONE);
        }

        return slice;
    }
    @Override
    public List<FeatureGenerator> getFeatures() {
        return Arrays.asList(new AetherCaveGenerator(), new CloudGenerator());
    }
}
