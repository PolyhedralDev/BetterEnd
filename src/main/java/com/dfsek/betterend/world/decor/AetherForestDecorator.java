package com.dfsek.betterend.world.decor;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;

public class AetherForestDecorator extends Decorator {
    private final ProbabilityCollection<Flora> Flora = new ProbabilityCollection<Flora>().add(FloraType.GRASS, 75)
            .add(FloraType.TALL_GRASS, 10)
            .add(FloraType.FERN, 5)
            .add(FloraType.TALL_FERN, 5)
            .add(FloraType.POPPY, 5)
            .add(FloraType.BLUE_ORCHID, 5);

    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(TreeType.GIANT_OAK, 1);

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    @Override
    public int getTreeDensity() {
        return 1;
    }

    @Override
    public boolean overrideStructureChance() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return Biome.END_HIGHLANDS;
    }

    @Override
    public ProbabilityCollection<Flora> getFlora() {
        return Flora;
    }

    @Override
    public int getFloraChance() {
        return 60;
    }


}
