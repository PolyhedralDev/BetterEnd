package com.dfsek.betterend.biomes.decor;

import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Decorator;

public class AetherDecorator implements Decorator<Structure> {
    private final ProbabilityCollection<Structure> structures = new ProbabilityCollection<Structure>()
            .add(Structure.AETHER_RUIN, 75)
            .add(Structure.WOOD_HOUSE, 12)
            .add(Structure.COBBLE_HOUSE, 12)
            .add(Structure.GOLD_DUNGEON, 1);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(Tree.OAK, 80)
            .add(Tree.LARGE_OAK, 15)
            .add(Tree.BIRCH, 5);

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
        return 3;
    }
    
    @Override
    public boolean shouldGenerateSnow() {
        return false;
    }
}
