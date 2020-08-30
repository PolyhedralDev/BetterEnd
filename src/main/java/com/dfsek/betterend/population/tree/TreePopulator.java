package com.dfsek.betterend.population.tree;

import com.dfsek.betterend.population.structures.SpawnRequirement;
import com.dfsek.betterend.population.tree.fractal.trees.OakTree;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TreePopulator extends BlockPopulator  {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {

        int x = random.nextInt(16);
        int z = random.nextInt(16);

        if(SpawnRequirement.getHighestBlock(chunk, x, z) < 0) return;

        OakTree tree = new OakTree(chunk.getBlock(8, 64, 8).getLocation(), random);
        tree.grow();
        tree.plant(false);
    }
}
