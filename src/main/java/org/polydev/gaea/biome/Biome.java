package org.polydev.gaea.biome;

import org.bukkit.World;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.tree.Tree;

import java.util.List;
import java.util.Random;

/**
 * Interface to be implemented by a custom generator's Biome enum.<br>
 *     Represents a custom biome, and contains methods to retrieve information about each type.
 */
public interface Biome {
    /**
     * Fetches a random structure from the ProbabilityCollection using the provided Random instance.
     * @param r The Random instance to use.
     * @return Structure - A structure to be generated in the biome.
     */
    Structure getRandomStructure(World w, Random r);

    /**
     * Gets a random tree from the ProbabilityCollection using the provided Random instance.
     * @param r The Random instance to use.
     * @return Tree - A tree to be generated in the biome.
     */
    Tree getTree(Random r);

    /**
     * Gets the tree density of the biome.
     * @return int - The percentage tree density (out of 100)
     */
    int getTreeDensity();

    /**
     * Whether or not to override the world's configured structure chance.
     * @return boolean - True if chance is to be overridden.
     */
    boolean overrideStructureChance();

    /**
     * Gets the Vanilla biome to represent the custom biome.
     * @return Biome - The Vanilla biome.
     */
    org.bukkit.block.Biome getVanillaBiome();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     * @return BiomeTerrain - The terrain generation instance.
     */
    BiomeTerrain getGenerator();

    /**
     * Gets a list of Structure Features to be applied to all structures in the biome.
     * @return List&lt;Feature&gt; - The list of Features.
     */
    List<Feature> getStructureFeatures();

    /**
     * Returns the Decorator instance containing information about the population in the biome.
     * @return Decorator - the Decorator instance.
     */
    Decorator getDecorator();
}
