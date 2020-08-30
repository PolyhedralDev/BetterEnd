package com.dfsek.betterend.biomes;

import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;

public abstract class BiomeTerrain {
    public BiomeTerrain() {
    }
    
    public abstract double getNoise(FastNoise gen, int x, int z);
    public abstract BlockPalette getPalette();
}
