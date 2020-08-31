package org.polydev.gaea.world.palette;

import org.polydev.gaea.math.ProbabilityCollection;
import org.bukkit.Material;

import java.util.Random;

/**
 * Class representation of a layer of a BlockPalette.
 */
public class PaletteLayer {
    private ProbabilityCollection<Material> collection;
    private Material m;
    private final boolean col;
    private final int layers;

    /**
     * Constructs a PaletteLayer with a ProbabilityCollection of materials and a number of layers.
     * @param type - The collection of materials to choose from.
     * @param layers - The number of layers.
     */
    public PaletteLayer(ProbabilityCollection<Material> type, int layers) {
        this.col = true;
        this.collection = type;
        this.layers = layers;
    }

    /**
     * Constructs a PaletteLayer with a single Material and a number of layers.
     * @param type - The material to use.
     * @param layers - The number of layers.
     */
    public PaletteLayer(Material type, int layers) {
        this.col = false;
        this.m = type;
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
     * Gets a material from the layer.
     * @return Material - the material..
     */
    public Material get(Random random) {
        if(col) return this.collection.get(random);
        return m;
    }
}
