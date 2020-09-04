package org.polydev.gaea.math;

import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;

public class ChunkInterpolator {
    Interpolator[][] interpGrid = new Interpolator[4][4];
    public ChunkInterpolator(int chunkX, int chunkZ, BiomeGrid<? extends Biome> grid, FastNoise noise) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                interpGrid[x][z] = new Interpolator(grid.getBiome(xOrigin + x * 4, zOrigin + z * 4).getGenerator().getNoise(noise, xOrigin + x * 4, zOrigin + z * 4) * 2.0f,
                        grid.getBiome(xOrigin + x * 4 + 4, zOrigin + z * 4).getGenerator().getNoise(noise, xOrigin + x * 4 + 4, zOrigin + z * 4) * 2.0f,
                        grid.getBiome(xOrigin + x * 4, zOrigin + z * 4 + 4).getGenerator().getNoise(noise, xOrigin + x * 4, zOrigin + z * 4 + 4) * 2.0f,
                        grid.getBiome(xOrigin + x * 4 + 4, zOrigin + z * 4 + 4).getGenerator().getNoise(noise, xOrigin + x * 4 + 4, zOrigin + z * 4 + 4) * 2.0f);
            }
        }
    }

    public double getNoise(byte x, byte z) {
        return interpGrid[x/4][z/4].bilerp((float)(x%4)/4, (float)(z%4)/4);
    }
}
