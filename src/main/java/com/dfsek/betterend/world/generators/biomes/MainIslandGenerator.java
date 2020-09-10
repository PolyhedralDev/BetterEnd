package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public class MainIslandGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public MainIslandGenerator() {
        super();
        this.palette = new BlockPalette().add(Material.END_STONE, 1);
    }

    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoise instance.
     *
     * @param gen - The FastNoise instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        return ((gen.getSimplexFractal(x, z) + 0.75) / 1.25) * (Math.sqrt(- (Math.pow(x, 2) + Math.pow(z, 2)) + 10000)) / 96;
    }

    @Override
    public double getNoise(FastNoise fastNoise, int i, int i1, int i2) {
        return getNoise(fastNoise, i, i1);
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public BlockPalette getPalette(int y) {
        return this.palette;
    }
}
