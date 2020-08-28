package com.dfsek.betterend.biomes.generators.border;

import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.biomes.BiomeTerrain;
import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;
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
        return gen.getSimplexFractal(x, z) * 0.5f;
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }
}
