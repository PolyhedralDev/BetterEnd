package org.polydev.gaea.math;

import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;

public class ChunkInterpolator {
    private final BiomeGrid<? extends Biome> grid;
    private final int xOrigin;
    private final int zOrigin;
    private final FastNoise noise;
    public ChunkInterpolator(int chunkX, int chunkZ, BiomeGrid<? extends Biome> grid, FastNoise noise) {
        this.grid = grid;
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.noise = noise;
    }

    public double getNoise(byte x, byte z) {
        int flooredX = ((int)x/4)*4;
        int flooredZ = ((int)z/4)*4;
        Interpolator interp = new Interpolator(
                grid.getBiome(xOrigin + flooredX, zOrigin + flooredZ).getGenerator().getNoise(noise, xOrigin + flooredX, zOrigin + flooredZ) * 2.0f,
                grid.getBiome(xOrigin + flooredX + 3, zOrigin + flooredZ).getGenerator().getNoise(noise,xOrigin + flooredX + 3, zOrigin + flooredZ) * 2.0f,
                grid.getBiome(xOrigin + flooredX, zOrigin + flooredZ + 3).getGenerator().getNoise(noise,xOrigin + flooredX, zOrigin + flooredZ + 3) * 2.0f,
                grid.getBiome(xOrigin + flooredX + 3, zOrigin + flooredZ + 3).getGenerator().getNoise(noise,xOrigin + flooredX + 3, zOrigin + flooredZ + 3) * 2.0f);
        return interp.bilerp((float)(x%4)/3, (float)(z%4)/3);
    }
}
