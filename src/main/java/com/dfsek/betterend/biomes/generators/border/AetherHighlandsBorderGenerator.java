package com.dfsek.betterend.biomes.generators.border;

import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.terrain2.BiomeTerrain;
import org.polydev.gaea.world.palette.BlockPalette;
import org.polydev.gaea.math.FastNoise;
import org.bukkit.Material;

public class AetherHighlandsBorderGenerator extends BiomeTerrain {
    private final BlockPalette palette;
    public AetherHighlandsBorderGenerator() {
        super();
        this.palette = new BlockPalette()
                .add(new ProbabilityCollection<Material>()
                        .add(Material.GRASS_BLOCK, 75)
                        .add(Material.COARSE_DIRT, 5)
                        .add(Material.GRAVEL, 7)
                        .add(Material.PODZOL, 13), 1)
                .add(Material.DIRT, 2)
                .add(Material.STONE, 1);
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
