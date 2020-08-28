package com.dfsek.betterend.biomes.generators.border;

import com.dfsek.betterend.biomes.BiomeTerrain;
import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;
import org.bukkit.Material;

public class VoidEndBorderGenerator extends BiomeTerrain {
    private final BlockPalette palette;
    public VoidEndBorderGenerator() {
        super();
        this.palette = new BlockPalette().add(Material.END_STONE, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return gen.getSimplexFractal(x, z)*0.5f;
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
