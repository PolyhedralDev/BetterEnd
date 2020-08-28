package com.dfsek.betterend.biomes.generators;

import com.dfsek.betterend.biomes.BiomeTerrain;
import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;
import org.bukkit.Material;

public class VoidGenerator extends BiomeTerrain {
    private final BlockPalette palette;
    public VoidGenerator() {
        super();
        this.palette = new BlockPalette()
                .add(Material.AIR, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return 0.4;
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
