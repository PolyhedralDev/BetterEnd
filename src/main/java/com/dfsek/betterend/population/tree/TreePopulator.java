package com.dfsek.betterend.population.tree;

import co.aikar.taskchain.TaskChainFactory;
import com.dfsek.betterend.population.structures.SpawnRequirement;
import com.dfsek.betterend.population.tree.fractal.trees.OakTree;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TreePopulator extends BlockPopulator  {
    private TaskChainFactory genChain;

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {

        int x = random.nextInt(16);
        int z = random.nextInt(16);
        int y = SpawnRequirement.getHighestBlock(chunk, x, z);
        if(y <= 0) return;

        OakTree tree = new OakTree(chunk.getBlock(8, y, 8).getLocation(), random);

        genChain.newChain()
                .async(tree::grow)
                .sync(() -> tree.plant(false))
                .execute();
    }
}
