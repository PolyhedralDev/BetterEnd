package com.dfsek.betterend.biomes;

import com.dfsek.betterend.generation.BlockPalette;
import com.dfsek.betterend.generation.FastNoise;
import org.bukkit.World;

public abstract class BiomeTerrain {
    public BiomeTerrain() {
    }
    
    public abstract double getNoise(FastNoise gen, int x, int z);
    public abstract BlockPalette getPalette();
}
