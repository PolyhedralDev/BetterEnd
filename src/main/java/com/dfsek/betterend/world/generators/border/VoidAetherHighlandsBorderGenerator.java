package com.dfsek.betterend.world.generators.border;

import org.bukkit.Material;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.BlockPalette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.Random;


public class VoidAetherHighlandsBorderGenerator extends BiomeTerrain {
    private final BlockPalette palette;

    public VoidAetherHighlandsBorderGenerator() {
        super();
        this.palette = new RandomPalette(new Random(2403))
                .add(new ProbabilityCollection<Material>()
                        .add(Material.GRASS_BLOCK, 50)
                        .add(Material.COARSE_DIRT, 10)
                        .add(Material.GRAVEL, 15)
                        .add(Material.PODZOL, 25), 1)
                .add(Material.DIRT, 2)
                .add(Material.STONE, 1);
    }

    @Override
    public double getNoise(FastNoise gen, World w, int x, int z) {
        return gen.getNoise(x, z) * 0.5f;
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
