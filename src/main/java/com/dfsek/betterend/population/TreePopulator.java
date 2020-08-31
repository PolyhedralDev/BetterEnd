package com.dfsek.betterend.population;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.biomes.Biome;
import com.dfsek.betterend.biomes.EndBiomeGrid;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class TreePopulator extends BlockPopulator  {
    private final BetterEnd main = BetterEnd.getInstance();
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        int numTrees = 0;
        for(int i = 0; i < 10; i++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = WorldUtil.getHighestValidSpawnAt(chunk, x, z);
            if(y <= 0) continue;
            Location origin = chunk.getBlock(x, y, z).getLocation().add(0, 1, 0);
            Biome b = EndBiomeGrid.fromWorld(world).getBiome(origin);
            numTrees++;
            try {
                b.getTree(random).plant(origin, random, false, main);
            } catch(NullPointerException ignored) {}
            if(numTrees >= b.getTreeDensity()) return;
        }
    }
}
