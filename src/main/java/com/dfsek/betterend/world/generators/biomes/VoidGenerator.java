package com.dfsek.betterend.world.generators.biomes;

import org.bukkit.Material;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.palette.BlockPalette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Random;


public class VoidGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public VoidGenerator() {
        super();
        this.palette = new RandomPalette(new Random(2403))
                .add(Material.AIR, 1);
    }

    @Override
    public double getNoise(FastNoise gen, World w, int x, int z) {
        return 0;
    }

    @Override
    public double getNoise(FastNoise fastNoise, World w, int i, int i1, int i2) {
        return getNoise(fastNoise, w, i, i2);
    }

    @Override
    public BlockPalette getPalette(int y) {
        return this.palette;
    }
}
