package com.dfsek.betterend.world.decor;

import com.dfsek.betterend.population.structures.EndStructure;
import org.bukkit.block.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;

public class MainIslandDecorator implements Decorator<EndStructure> {
    @Override
    public ProbabilityCollection<EndStructure> getStructures() {
        return new ProbabilityCollection<>();
    }

    @Override
    public ProbabilityCollection<Tree> getTrees() {
        return new ProbabilityCollection<>();
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
        return Biome.THE_END;
    }
}
