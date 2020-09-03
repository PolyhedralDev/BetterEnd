package com.dfsek.betterend.population;

import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Random;

public class SnowPopulator extends BlockPopulator {
    private static final BlockData snow = Material.SNOW.createBlockData();
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture featureFuture = EndProfiler.fromWorld(world).measure("SnowTime");
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                Location l = chunk.getBlock(x, 0, z).getLocation();
                if(EndBiomeGrid.fromWorld(world).getBiome(l).shouldGenerateSnow()) {
                    Block highest = world.getHighestBlockAt(l);
                    if(highest.getLocation().getBlockY() > 1 && highest.getLocation().add(0,1,0).getBlock().isEmpty()) {
                        if(random.nextInt() < 40) highest.getLocation().add(0,1,0).getBlock().setBlockData(snow, false);
                    }
                }
            }
        }
        if(featureFuture != null) featureFuture.complete();
    }
}
