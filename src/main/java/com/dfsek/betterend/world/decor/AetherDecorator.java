package com.dfsek.betterend.world.decor;

import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Fauna;
import org.polydev.gaea.world.FaunaType;

public class AetherDecorator extends Decorator {
    private final ProbabilityCollection<Fauna> fauna = new ProbabilityCollection<Fauna>().add(FaunaType.GRASS, 75)
            .add(FaunaType.TALL_GRASS, 10)
            .add(FaunaType.FERN, 5)
            .add(FaunaType.TALL_FERN, 5)
            .add(FaunaType.POPPY, 5)
            .add(FaunaType.BLUE_ORCHID, 5);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(TreeType.OAK, 80)
            .add(TreeType.LARGE_OAK, 15)
            .add(TreeType.BIRCH, 5);

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    @Override
    public int getTreeDensity() {
        return 3;
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
        return Biome.END_HIGHLANDS;
    }

    @Override
    public ProbabilityCollection<Fauna> getFauna() {
        return fauna;
    }

    @Override
    public int getFaunaChance() {
        return 60;
    }
}
