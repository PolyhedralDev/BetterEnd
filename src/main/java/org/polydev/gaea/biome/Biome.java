package org.polydev.gaea.biome;

import com.dfsek.betterend.population.structures.EndStructure;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.tree.Tree;

import java.util.List;
import java.util.Random;

public interface Biome {
    EndStructure getRandomStructure(Random r);
    Tree getTree(Random r);
    int getTreeDensity();
    boolean overrideStructureChance();
    org.bukkit.block.Biome getVanillaBiome();
    BiomeTerrain getGenerator();
    List<Feature> getStructureFeatures();
    Decorator<?> getDecorator();
}
