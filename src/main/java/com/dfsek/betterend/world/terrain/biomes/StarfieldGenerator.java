package com.dfsek.betterend.world.terrain.biomes;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.ChunkSlice;
import com.dfsek.betterend.world.terrain.FeatureGenerator;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class StarfieldGenerator extends BiomeGenerator {
    public StarfieldGenerator(World world) {
        super(world);
    }

    @Override
    public int getMaxHeight(int x, int z) {
        return 63;
    }

    @Override
    public int getMinHeight(int x, int z) {
        return 64;
    }

    @Override
    public ChunkSlice generateSlice(byte x, byte z, int chunkX, int chunkZ) {
        return new ChunkSlice(x, z);
    }
    @Override
    public List<FeatureGenerator> getFeatures() {
        return null;
    }
}
