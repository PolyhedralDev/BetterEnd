package org.polydev.gaea.util;

import org.bukkit.Chunk;
import org.bukkit.Material;

public class WorldUtil {
    public static int getHighestValidSpawnAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK
                && chunk.getBlock(x, y, z).getType() != Material.GRAVEL
                && chunk.getBlock(x, y, z).getType() != Material.PODZOL
                && chunk.getBlock(x, y, z).getType() != Material.END_STONE
                && chunk.getBlock(x, y, z).getType() != Material.DIRT
                && chunk.getBlock(x, y, z).getType() != Material.STONE
                && chunk.getBlock(x, y, z).getType() != Material.COARSE_DIRT) && y > 0; y--);
        return y;
    }
}
