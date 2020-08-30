package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.biomes.BiomeGrid;
import com.dfsek.betterend.world.WorldConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructurePopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if(WorldConfig.fromWorld(world).structureChance < random.nextInt(100)) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y;
            for(y = world.getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK
                    && chunk.getBlock(x, y, z).getType() != Material.GRAVEL
                    && chunk.getBlock(x, y, z).getType() != Material.PODZOL
                    && chunk.getBlock(x, y, z).getType() != Material.END_STONE
                    && chunk.getBlock(x, y, z).getType() != Material.DIRT
                    && chunk.getBlock(x, y, z).getType() != Material.STONE
                    && chunk.getBlock(x, y, z).getType() != Material.COARSE_DIRT) && y > 0; y--);
            if(y <= 0) return;
            x += (chunk.getX() << 4);
            z += (chunk.getZ() << 4);
            Structure struc = BiomeGrid.fromWorld(world).getBiome(x, z).getRandomStructure(random);
            if(struc == null) return;
            System.out.println(struc);
            if(struc.getSpawnRequirement().isSky()) y = 96;
            NMSStructure nms = struc.getInstance(new Location(world, x, y, z), random);
            nms.setRotation(random.nextInt(4)*90);
            if(struc.getSpawnRequirement().isValidSpawn(nms)) nms.paste();
            System.out.println("Generated...");
        }
    }
}
