package com.dfsek.betterend.world.decor;

import com.dfsek.betterend.population.structures.EndStructure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.biome.Decorator;

public class ShatteredEndDecorator implements Decorator<EndStructure> {
    private final ProbabilityCollection<EndStructure> structures = new ProbabilityCollection<EndStructure>()
            .add(EndStructure.END_RUIN, 85)
            .add(EndStructure.END_SHIP, 1)
            .add(EndStructure.STRONGHOLD, 14);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(Tree.SMALL_SHATTERED_PILLAR, 60)
            .add(Tree.LARGE_SHATTERED_PILLAR, 40);

    @Override
    public ProbabilityCollection<EndStructure> getStructures() {
        return structures;
    }

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
}
