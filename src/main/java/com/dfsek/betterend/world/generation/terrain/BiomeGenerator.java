package com.dfsek.betterend.world.generation.terrain;

import org.bukkit.Location;

public interface BiomeGenerator {
    int getMaxHeight(Location l);
    int getMinHeight(Location l);
}
