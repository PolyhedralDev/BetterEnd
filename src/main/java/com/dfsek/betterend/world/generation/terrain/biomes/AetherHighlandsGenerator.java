package com.dfsek.betterend.world.generation.terrain.biomes;

import com.dfsek.betterend.world.generation.terrain.BiomeGenerator;
import org.bukkit.Location;

public class AetherHighlandsGenerator implements BiomeGenerator {
    @Override
    public int getMaxHeight(Location l) {
        return 79;
    }

    @Override
    public int getMaxHeight(int x, int z) {
        return 79;
    }
}
