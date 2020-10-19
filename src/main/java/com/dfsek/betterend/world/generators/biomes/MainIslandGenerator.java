package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Random;


public class MainIslandGenerator extends Generator {
    @Override
    public boolean useMinimalInterpolation() {
        return true;
    }
    private final Palette<BlockData> palette;

    public MainIslandGenerator() {
        super();
        this.palette = new RandomPalette<BlockData>(new Random(2403)).add(Material.END_STONE.createBlockData(), 1);
    }

    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int z) {
        return ((gen.getNoise(x, z) + 0.75) / 1.25) * (Math.sqrt(- (Math.pow(x, 2) + Math.pow(z, 2)) + 10000)) / 96;
    }

    @Override
    public double getNoise(FastNoiseLite fastNoise, World w, int i, int i1, int i2) {
        return getNoise(fastNoise, w, i, i2);
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public Palette<BlockData> getPalette(int y) {
        return this.palette;
    }
}
