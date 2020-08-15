package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import com.dfsek.betterend.world.terrain.features.AetherCaveGenerator;
import com.dfsek.betterend.world.terrain.features.CloudGenerator;
import com.dfsek.betterend.world.terrain.features.EndCaveGenerator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class EndGenerator implements BiomeGenerator {

    @Override
    public int getMaxHeight(int x, int z, long seed) {
        return 64;
    }

    @Override
    public int getMinHeight(int x, int z, long seed) {
        return 32;
    }

    @Override
    public ChunkSlice generateSlice(int min, int max) {
        ChunkSlice slice = new ChunkSlice();
        for(int y = min; y < max; y++) {
            slice.setBlock(y, Material.END_STONE);
        }
        return slice;
    }

    @Override
    public List<FeatureGenerator> getFeatures() {
        return Arrays.asList(new EndCaveGenerator());
    }
}
