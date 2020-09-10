package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public class AetherGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public AetherGenerator() {
        super();
        this.palette = new BlockPalette()
                .add(Material.GRASS_BLOCK, 1)
                .add(Material.DIRT, 2)
                .add(Material.STONE, 1);
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
