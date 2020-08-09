package com.dfsek.betterend.world.generation.terrain.biomes;

import com.dfsek.betterend.world.generation.terrain.BiomeGenerator;
import org.bukkit.Location;

public class AetherHighlandsForestGenerator implements BiomeGenerator {
    @Override
    public int getMaxHeight(Location l) {
        return 26;
    }

    @Override
    public int getMinHeight(Location L) {
        return 20;
    }
}
