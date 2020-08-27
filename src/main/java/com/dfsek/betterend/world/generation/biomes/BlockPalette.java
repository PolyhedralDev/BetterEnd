package com.dfsek.betterend.world.generation.biomes;

import com.dfsek.betterend.ProbabilityCollection;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockPalette {
    private final List<PaletteLayer> pallet = new ArrayList<>();

    public BlockPalette() {}
    public BlockPalette add(Material m, int layers) {
        pallet.add(new PaletteLayer(new ProbabilityCollection<Material>().add(m, 1), layers+(pallet.size() == 0 ? 0 : pallet.get(pallet.size()-1).getLayers())));
        return this;
    }
    public BlockPalette add(ProbabilityCollection<Material> m, int layers) {
        int l = 0;
        for(PaletteLayer p : pallet) {
            l+=p.getLayers();
        }
        pallet.add(new PaletteLayer(m, layers+l));
        return this;
    }

    public Material get(int layer, Random random) {
        for(PaletteLayer p : pallet) {
            if(layer < p.getLayers()) return p.getCollection().get(random);

        }
        return pallet.get(pallet.size()-1).getCollection().get(random);
    }
}
