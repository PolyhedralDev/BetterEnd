package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.biomes.EndBiomeGrid;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class StructurePopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if(50 > random.nextInt(100)) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = WorldUtil.getHighestValidSpawnAt(chunk, x, z);
            x += (chunk.getX() << 4);
            z += (chunk.getZ() << 4);
            Structure struc = EndBiomeGrid.fromWorld(world).getBiome(x, z).getRandomStructure(random);
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
            System.out.println("Generated " + struc + " at " + x + " " + origin.getY() + " " + z);
        }
    }
}
