package com.dfsek.betterend.biomes.decor;

import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Decorator;

public class StarfieldDecorator implements Decorator<Structure> {
    private final ProbabilityCollection<Structure> structures = new ProbabilityCollection<Structure>().add(Structure.VOID_STAR, 1);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();

    @Override
    public ProbabilityCollection<Structure> getStructures() {
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
    public boolean shouldGenerateSnow() {
        return false;
    }
}
