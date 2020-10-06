package com.dfsek.betterend.population;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.ArrayList;
import java.util.Random;

public class SnowPopulator extends GaeaBlockPopulator {
    private static final ArrayList<Material> blacklistSpawn = new ArrayList<>();
    private static final BlockData snow = Material.SNOW.createBlockData();

    static {
        for(Material m : Material.values()) {
            String name = m.toString().toLowerCase();
            if(name.contains("slab")
                    || name.contains("stair")
                    || name.contains("wall")
                    || name.contains("fence")
                    || name.contains("lantern")
                    || name.contains("chest")
                    || name.contains("door")
                    || name.contains("repeater")
                    || name.equals("lily_pad")
                    || name.equals("snow")
                    || name.equals("pane")) blacklistSpawn.add(m);
        }
        blacklistSpawn.add(Material.END_STONE);
        if(ConfigUtil.debug)
            Bukkit.getLogger().info("Added " + blacklistSpawn.size() + " materials to snow blacklist");
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture featureFuture = EndProfiler.fromWorld(world).measure("SnowTime");
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                Location l = chunk.getBlock(x, 0, z).getLocation();
                if(EndBiomeGrid.fromWorld(world).getBiome(l).shouldGenerateSnow()) {
                    Block highest = world.getHighestBlockAt(l);
                    if(highest.getLocation().getBlockY() > 1 && highest.getLocation().add(0, 1, 0).getBlock().isEmpty()) {
                        if(random.nextInt() < 40 && ! blacklistSpawn.contains(highest.getType()))
                            highest.getLocation().add(0, 1, 0).getBlock().setBlockData(snow, false);
                    }
                }
            }
        }
        if(featureFuture != null) featureFuture.complete();
    }
}
