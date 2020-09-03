package com.dfsek.betterend.world.generators.border;

import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.world.palette.BlockPalette;
import org.polydev.gaea.math.FastNoise;
import org.bukkit.Material;

public class VoidAetherBorderGenerator extends BiomeTerrain {
    private final BlockPalette palette;
    public VoidAetherBorderGenerator() {
        super();
        this.palette = new BlockPalette()
                .add(Material.GRASS_BLOCK, 1)
                .add(Material.DIRT, 2)
                .add(Material.STONE, 1);
    }

    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return gen.getSimplexFractal(x, z)-0.15f;
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
