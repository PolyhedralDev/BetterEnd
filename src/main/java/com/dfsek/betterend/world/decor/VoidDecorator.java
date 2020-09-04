package com.dfsek.betterend.world.decor;

import com.dfsek.betterend.population.structures.EndStructure;
import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Fauna;

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

    @Override
    public Biome getVanillaBiome() {
        return Biome.END_MIDLANDS;
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
