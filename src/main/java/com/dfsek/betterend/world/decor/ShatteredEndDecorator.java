package com.dfsek.betterend.world.decor;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Fauna;

public class ShatteredEndDecorator extends Decorator {
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(TreeType.SMALL_SHATTERED_PILLAR, 60)
            .add(TreeType.LARGE_SHATTERED_PILLAR, 40);

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    @Override
    public int getTreeDensity() {
        return 4;
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
