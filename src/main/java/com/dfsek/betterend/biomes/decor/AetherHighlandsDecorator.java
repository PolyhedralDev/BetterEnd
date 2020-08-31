package com.dfsek.betterend.biomes.decor;

import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Decorator;

public class AetherHighlandsDecorator implements Decorator<Structure> {
    private final ProbabilityCollection<Structure> structures = new ProbabilityCollection<Structure>()
            .add(Structure.AETHER_RUIN, 75)
            .add(Structure.SPRUCE_WOOD_HOUSE, 11)
            .add(Structure.COBBLE_HOUSE, 11)
            .add(Structure.GOLD_DUNGEON, 3);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(Tree.SPRUCE, 80)
            .add(Tree.LARGE_SPRUCE, 20);

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
        return 6;
    }
    
    @Override
    public boolean shouldGenerateSnow() {
        return true;
    }
}
