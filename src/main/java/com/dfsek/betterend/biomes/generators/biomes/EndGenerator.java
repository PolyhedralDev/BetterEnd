package com.dfsek.betterend.biomes.generators.biomes;

import org.polydev.gaea.terrain2.BiomeTerrain;
import org.polydev.gaea.world.palette.BlockPalette;
import org.polydev.gaea.math.FastNoise;
import org.bukkit.Material;

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
    public BlockPalette getPalette() {
        return this.palette;
    }
}
