package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Random;


public class AetherHighlandsGenerator extends Generator {
    @Override
    public boolean useMinimalInterpolation() {
        return true;
    }
    private final Palette<BlockData> palette;

    public AetherHighlandsGenerator() {
        super();
        this.palette = new RandomPalette<BlockData>(new Random(2403))
                .add(new ProbabilityCollection<BlockData>()
                        .add(Material.GRASS_BLOCK.createBlockData(), 50)
                        .add(Material.COARSE_DIRT.createBlockData(), 10)
                        .add(Material.GRAVEL.createBlockData(), 15)
                        .add(Material.PODZOL.createBlockData(), 25), 1)
                .add(Material.DIRT.createBlockData(), 2)
                .add(Material.STONE.createBlockData(), 1);
    }

    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int z) {
        return gen.getNoise(x, z);
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
