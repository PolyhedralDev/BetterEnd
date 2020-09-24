package com.dfsek.betterend.world.decor;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;

public class MainIslandDecorator extends Decorator {
    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return new ProbabilityCollection<>();
    }

    @Override
    public int getTreeDensity() {
        return 0;
    }

    @Override
    public boolean overrideStructureChance() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return Biome.THE_END;
    }

    @Override
    public ProbabilityCollection<Flora> getFlora() {
        return new ProbabilityCollection<>();
    }

    @Override
    public int getFloraChance() {
        return 0;
    }
}
