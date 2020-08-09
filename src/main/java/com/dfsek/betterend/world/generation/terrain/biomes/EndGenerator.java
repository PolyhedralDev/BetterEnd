package com.dfsek.betterend.world.generation.terrain.biomes;

import com.dfsek.betterend.world.generation.terrain.BiomeGenerator;
import org.bukkit.Location;

public class EndGenerator implements BiomeGenerator {
    @Override
    public int getMaxHeight(Location l) {
        return 64;
    }

    @Override
    public int getMinHeight(Location l) {
        return 50;
    }
}
