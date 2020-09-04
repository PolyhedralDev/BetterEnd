package org.polydev.gaea.generation;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoise;

import java.util.List;
import java.util.Random;

public abstract class GaeaChunkGenerator extends ChunkGenerator {
    private FastNoise gen;
    private ChunkInterpolator interp;

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        if (gen == null) {
            gen = new FastNoise((int) world.getSeed());
            gen.setNoiseType(FastNoise.NoiseType.SimplexFractal);
            gen.setFractalOctaves(getNoiseOctaves(world));
            gen.setFrequency(getNoiseFrequency(world));
        }
        interp = new ChunkInterpolator(chunkX, chunkZ, this.getBiomeGrid(world), gen);
        ChunkData chunk = generateBase(world, random, chunkX, chunkZ, gen);
        for(GenerationPopulator g : getGenerationPopulators(world)) {
            chunk = g.populate(world, chunk, random, chunkX, chunkZ);
        }
        for(byte x = 0; x < 4; x++) {
            for (byte z = 0; z < 4; z++) {
                biome.setBiome(x, z, getBiomeGrid(world).getBiome((chunkX << 4) + x, (chunkZ << 4) + z).getVanillaBiome());
            }
        }
        return chunk;
    }

    public double getInterpolatedNoise(byte x, byte z) {
        return this.interp.getNoise(x, z);
    }

    public abstract ChunkData generateBase(@NotNull World world, @NotNull Random random, int x, int z, FastNoise noise);
    public abstract int getNoiseOctaves(World w);
    public abstract float getNoiseFrequency(World w);
    public abstract List<GenerationPopulator> getGenerationPopulators(World w);
    public abstract org.polydev.gaea.biome.BiomeGrid<? extends Biome> getBiomeGrid(World w);
}
