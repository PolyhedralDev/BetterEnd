package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public class EndGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public EndGenerator() {
        super();
        this.palette = new BlockPalette().add(Material.END_STONE, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return gen.getSimplexFractal(x, z);
    }

    @Override
    public double getNoise(FastNoise fastNoise, int i, int i1, int i2) {
        return getNoise(fastNoise, i, i1);
    }

    @Override
    public BlockPalette getPalette(int y) {
        return this.palette;
    }
}
