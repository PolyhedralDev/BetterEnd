package com.dfsek.betterend.population;

import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Flora;

import java.util.Random;

public class FloraPopulator extends GaeaBlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture featureFuture = EndProfiler.fromWorld(world).measure("FloraTime");
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                EndBiome biome = EndBiomeGrid.fromWorld(world).getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                if(biome.getDecorator().getFloraChance() <= 0 || random.nextInt(100) > biome.getDecorator().getFloraChance())
                    continue;
                Flora Flora = biome.getDecorator().getFlora().get(random);
                Block highest = Flora.getHighestValidSpawnAt(chunk, x, z);
                if(highest != null) Flora.plant(highest.getLocation());
            }
        }
        if(featureFuture != null) featureFuture.complete();
    }
}
