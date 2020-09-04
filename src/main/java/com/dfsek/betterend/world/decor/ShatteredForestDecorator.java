package com.dfsek.betterend.world.decor;

import com.dfsek.betterend.population.structures.EndStructure;
import org.bukkit.block.Biome;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.world.Fauna;

public class ShatteredForestDecorator implements Decorator<EndStructure> {
    private final ProbabilityCollection<EndStructure> structures = new ProbabilityCollection<EndStructure>()
            .add(EndStructure.END_RUIN, 85)
            .add(EndStructure.END_SHIP, 1)
            .add(EndStructure.STRONGHOLD, 14);
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<Tree>()
            .add(Tree.SHATTERED_LARGE, 30)
            .add(Tree.SHATTERED_SMALL, 70);

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
