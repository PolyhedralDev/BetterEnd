package org.polydev.gaea.biome;

import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;

public interface Decorator<S> {
    ProbabilityCollection<S> getStructures();
    ProbabilityCollection<Tree> getTrees();
    int getTreeDensity();
    boolean overrideStructureChance();
    boolean shouldGenerateSnow();
}
