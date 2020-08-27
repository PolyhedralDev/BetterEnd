package com.dfsek.betterend.world.generation.biomes;

import com.dfsek.betterend.ProbabilityCollection;
import org.bukkit.Material;

/**
 * Class representation of a layer of a BlockPalette.
 */
public class PaletteLayer {
    private final ProbabilityCollection<Material> collection;
    private final int layers;

    /**
     * Constructs a PaletteLayer with a ProbabilityCollection of materials and a number of layers.
     * @param type - The collection of materials to choose from.
     * @param layers - The number of layers.
     */
    public PaletteLayer(ProbabilityCollection<Material> type, int layers) {
        this.collection = type;
        this.layers = layers;
    }

    /**
     * Gets the number of layers.
     * @return int - the number of layers.
     */
    public int getLayers() {
        return layers;
    }

    /**
     * Gets the ProbabilityCollection of materials.
     * @return ProbabilityCollection of materials.
     */
    public ProbabilityCollection<Material> getCollection() {
        return collection;
    }
}
