package org.polydev.gaea.biome;

import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Fauna;

public interface Decorator<S> {
    ProbabilityCollection<S> getStructures();
    ProbabilityCollection<Tree> getTrees();
    int getTreeDensity();
    boolean overrideStructureChance();
    boolean shouldGenerateSnow();
    org.bukkit.block.Biome getVanillaBiome();
    ProbabilityCollection<Fauna> getFauna();
    int getFaunaChance();
}
