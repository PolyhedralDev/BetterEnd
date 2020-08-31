package com.dfsek.betterend.biomes.decor;

import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Decorator;

public class EndDecorator implements Decorator<Structure> {
    private final ProbabilityCollection<Structure> structures = new ProbabilityCollection<Structure>()
            .add(Structure.END_RUIN, 75)
            .add(Structure.END_HOUSE, 10)
            .add(Structure.END_TOWER, 6)
            .add(Structure.END_SHIP, 2)
            .add(Structure.STRONGHOLD, 7);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(Tree.CHORUS_PLANT, 80);

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
        return 4;
    }
    
    @Override
    public boolean shouldGenerateSnow() {
        return false;
    }
}
