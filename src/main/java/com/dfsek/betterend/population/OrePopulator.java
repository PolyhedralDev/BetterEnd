package com.dfsek.betterend.population;

import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Ore;

import java.util.Random;

public class OrePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture oreP = EndProfiler.fromWorld(world).measure("OreTime");
        WorldConfig config = WorldConfig.fromWorld(world);
        int x;
        int y;
        int z;
        for(int i = 0; i < config.oreAttempts; i++) { // Number of tries
            x = random.nextInt(15);
            z = random.nextInt(15);
            Ore ore;
            try {
                ore = config.ores.get(EndBiomeGrid.fromWorld(world).getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE)).get();
            } catch(NullPointerException e) {
                continue;
            }

            y = random.nextInt(Math.max(config.islandHeight - config.islandHeightMultiplierBottom + 1, 1)) + config.islandHeightMultiplierBottom;
            if(y > 1) doVein(world, chunk, random, new int[] {x, y, z}, ore.getType(), ore.getContChance());
        }
        if(oreP != null) oreP.complete();
    }

    private void doVein(World world, Chunk chunk, Random random, int[] coords, BlockData ore, int continueChance) {
        int x = coords[0];
        int y = coords[1];
        int z = coords[2];
        Material current = world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).getType();
        if(current == Material.STONE || current == Material.END_STONE) {
            boolean isStone = true;
            while(isStone) {
                world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).setBlockData(ore, false);
                if(random.nextInt(100) < continueChance) {
                    switch(random.nextInt(6)) {
                        case 0:
                            x++;
                            break;
                        case 1:
                            y++;
                            break;
                        case 2:
                            z++;
                            break;
                        case 3:
                            x--;
                            break;
                        case 4:
                            y--;
                            break;
                        case 5:
                            z--;
                            break;
                        default:
                    }
                    current = world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).getType();
                    isStone = (current.equals(Material.STONE) || current.equals(Material.END_STONE) || current.equals(ore.getMaterial()));
                } else isStone = false;
            }
        }
    }

}
