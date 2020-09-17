package com.dfsek.betterend.world.decor;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Fauna;

public class ShatteredForestDecorator extends Decorator {
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(TreeType.SHATTERED_LARGE, 30)
            .add(TreeType.SHATTERED_SMALL, 70);

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
    public boolean shouldGenerateSnow() {
        return false;
    }

    @Override
    public Biome getVanillaBiome() {
        return Biome.END_BARRENS;
    }

    @Override
    public ProbabilityCollection<Fauna> getFauna() {
        return new ProbabilityCollection<>();
    }

    @Override
    public int getFaunaChance() {
        return 0;
    }


}
