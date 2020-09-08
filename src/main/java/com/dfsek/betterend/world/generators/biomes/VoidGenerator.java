package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public class VoidGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public VoidGenerator() {
        super();
        this.palette = new BlockPalette()
                .add(Material.AIR, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return 0;
    }

    @Override
    public double getNoise(FastNoise fastNoise, int i, int i1, int i2) {
        return getNoise(fastNoise, i, i1);
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
