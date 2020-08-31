package org.polydev.gaea.biome;

import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.tree.Tree;

import java.util.Random;

public interface BiomeTemplate {
    Structure getRandomStructure(Random r);
    Tree getTree(Random r);
    int getTreeDensity();
    BiomeTerrain getGenerator();
}
