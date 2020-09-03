package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.world.EndBiome;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class StructurePopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture gen = EndProfiler.fromWorld(world).measure("StructureGenTime");
        int x = random.nextInt(16);
        int z = random.nextInt(16);
        EndBiome biome = EndBiomeGrid.fromWorld(world).getBiome(x, z);
        if(WorldConfig.fromWorld(world).structureChancePerChunk > random.nextInt(100) || biome.overrideStructureChance()) {
            int y = WorldUtil.getHighestValidSpawnAt(chunk, x, z);
            x += (chunk.getX() << 4);
            z += (chunk.getZ() << 4);
            EndStructure struc = biome.getRandomStructure(random);
            if(struc == null) return;
            Location origin = struc.getSpawnInfo().getSpawnLocation(new Location(world, x, y, z), random);
            if(origin.getY() <= 0) return;
            NMSStructure nms = struc.getInstance(origin, random);
            nms.setRotation(random.nextInt(4)*90);
            if(struc.getSpawnInfo().isValidSpawn(nms)) nms.paste();
            else return;
            for(Feature f : struc.getFeatures()) {
                f.populate(nms, random);
            }
            if(ConfigUtil.debug) System.out.println("Generated " + struc + " at " + x + " " + origin.getY() + " " + z);
        }
        gen.complete();
    }
}
