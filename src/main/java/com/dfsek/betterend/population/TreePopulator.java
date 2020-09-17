package com.dfsek.betterend.population;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class TreePopulator extends GaeaBlockPopulator {
    private final BetterEnd main = BetterEnd.getInstance();

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture gen = EndProfiler.fromWorld(world).measure("TreeGenTime");
        int numTrees = 0;
        for(int i = 0; i < 10; i++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = WorldUtil.getHighestValidSpawnAt(chunk, x, z);
            if(y <= 0) continue;
            Location origin = chunk.getBlock(x, y, z).getLocation().add(0, 1, 0);
            EndBiome b = EndBiomeGrid.fromWorld(world).getBiome(origin);
            numTrees++;
            try {
                b.getDecorator().getTrees().get(random).plant(origin, random, false, main);
            } catch(NullPointerException ignored) {
            }
            if(numTrees >= b.getDecorator().getTreeDensity()) return;
        }
        if(gen != null) gen.complete();
    }
}
