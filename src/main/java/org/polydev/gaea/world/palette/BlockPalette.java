package org.polydev.gaea.world.palette;

import org.bukkit.Material;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public class BlockPalette {
    private final List<PaletteLayer> pallet = new ArrayList<>();


    /**
     * Constructs a blank palette.
     */
    public BlockPalette() {

    }

    /**
     * Adds a material to the palette, for a number of layers.
     *
     * @param m      - The material to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public BlockPalette add(Material m, int layers) {
        pallet.add(new PaletteLayer(m, layers + (pallet.size() == 0 ? 0 : pallet.get(pallet.size() - 1).getLayers())));
        return this;
    }

    /**
     * Adds a ProbabilityCollection to the palette, for a number of layers.
     *
     * @param m      - The ProbabilityCollection to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public BlockPalette add(ProbabilityCollection<Material> m, int layers) {
        pallet.add(new PaletteLayer(m, layers + (pallet.size() == 0 ? 0 : pallet.get(pallet.size() - 1).getLayers())));
        return this;
    }

    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     * @return - Material - The material fetched.
     */
    public Material get(int layer, Random random) {
        for(PaletteLayer p : pallet) {
            if(layer < p.getLayers()) return p.get(random);
        }
        return pallet.get(pallet.size() - 1).get(random);
    }
}
