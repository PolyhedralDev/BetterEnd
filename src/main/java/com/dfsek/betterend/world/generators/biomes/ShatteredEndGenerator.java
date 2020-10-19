package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Random;


public class ShatteredEndGenerator extends Generator {
    private final FastNoiseLite shattered;
    private final Palette<BlockData> palette;
    public ShatteredEndGenerator() {
        super();
        shattered = new FastNoiseLite();
        shattered.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        shattered.setFrequency(0.1f);
        shattered.setFractalOctaves(3);
        this.palette = new RandomPalette<BlockData>(new Random(2403)).add(Material.END_STONE.createBlockData(), 1);
    }

    @Override
    public boolean useMinimalInterpolation() {
        return true;
    }

    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int z) {
        return gen.getNoise(x, z) * 1.2 + 0.4 * shattered.getNoise(x, z);
    }

    @Override
    public double getNoise(FastNoiseLite fastNoise, World w, int i, int i1, int i2) {
        return getNoise(fastNoise, w, i, i2);
    }

    @Override
    public Palette<BlockData> getPalette(int y) {
        return this.palette;
    }
}
