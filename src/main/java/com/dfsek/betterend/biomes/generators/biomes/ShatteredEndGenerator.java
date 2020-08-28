package com.dfsek.betterend.biomes.generators.biomes;

import com.dfsek.betterend.biomes.BiomeTerrain;
import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;
import org.bukkit.Material;

public class ShatteredEndGenerator extends BiomeTerrain {
    private final BlockPalette palette;
    public ShatteredEndGenerator() {
        super();
        this.palette = new BlockPalette().add(Material.END_STONE, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return gen.getSimplexFractal(x, z);
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
