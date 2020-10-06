package com.dfsek.betterend.population;

import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Flora;

import java.util.List;
import java.util.Random;

public class FloraPopulator extends GaeaBlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignore = EndProfiler.fromWorld(world).measure("FloraTime")) {
            WorldConfig c = WorldConfig.fromWorld(world);
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    EndBiome biome = EndBiomeGrid.fromWorld(world).getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    if(biome.getDecorator().getFloraChance() <= 0 || random.nextInt(100) > biome.getDecorator().getFloraChance())
                        continue;
                    Flora Flora = biome.getDecorator().getFlora().get(random);
                    List<Block> at = Flora.getValidSpawnsAt(chunk, x, z, new Range(c.islandHeight - 1, c.islandHeight+ c.islandHeightMultiplierTop));
                    if(at.size() == 0) continue;
                    Block highest = at.get(0);
                    if(highest != null) Flora.plant(highest.getLocation());
                }
            }
        }
    }
}
