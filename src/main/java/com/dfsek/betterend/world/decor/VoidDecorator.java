package com.dfsek.betterend.world.decor;

import com.dfsek.betterend.population.structures.EndStructure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.biome.Decorator;

public class VoidDecorator implements Decorator<EndStructure> {
    private final ProbabilityCollection<EndStructure> structures = new ProbabilityCollection<>();
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();

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
        return 0;
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
