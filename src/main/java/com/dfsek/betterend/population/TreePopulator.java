package com.dfsek.betterend.population;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.population.structures.SpawnRequirement;
import org.polydev.gaea.tree.fractal.FractalTree;
import org.polydev.gaea.tree.fractal.trees.OakTree;
import org.polydev.gaea.tree.fractal.trees.SpruceTree;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TreePopulator extends BlockPopulator  {
    private final BetterEnd main = BetterEnd.getInstance();
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {

        int x = random.nextInt(16);
        int z = random.nextInt(16);
        int y = SpawnRequirement.getHighestBlock(chunk, x, z);
        if(y <= 0) return;
        FractalTree tree;

        if(random.nextBoolean()) tree = new OakTree(chunk.getBlock(8, y, 8).getLocation(), random);
        else tree = new SpruceTree(chunk.getBlock(8, y, 8).getLocation(), random);

        main.getFactory().newChain()
                .async(tree::grow)
                .sync(() -> tree.plant(false))
                .execute();
    }
}
