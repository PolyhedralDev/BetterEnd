package com.dfsek.betterend.world.generation.biomes;

import com.dfsek.betterend.ProbabilityCollection;
import org.bukkit.Material;

public class PaletteLayer {
    private final ProbabilityCollection<Material> collection;
    private final int layers;
    public PaletteLayer(ProbabilityCollection<Material> type, int layers) {
        this.collection = type;
        this.layers = layers;
    }
    public int getLayers() {
        return layers;
    }
    public ProbabilityCollection<Material> getCollection() {
        return collection;
    }
}
