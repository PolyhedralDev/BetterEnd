package com.dfsek.betterend.world;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.population.CustomStructurePopulator;
import com.dfsek.betterend.population.SnowPopulator;
import com.dfsek.betterend.population.TreePopulator;
import com.dfsek.betterend.population.structures.StructurePopulator;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends ChunkGenerator {
    private final BetterEnd main = BetterEnd.getInstance();

    private FastNoise gen;

    @NotNull
    @Override
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
        EndProfiler profiler = EndProfiler.fromWorld(world);
        ProfileFuture totalFuture = profiler.measure("TotalChunkGenTime");
        if (gen == null) {
            gen = new FastNoise((int) world.getSeed());
            gen.setNoiseType(FastNoise.NoiseType.SimplexFractal);
            gen.setFractalOctaves(WorldConfig.fromWorld(world).octaves);
            gen.setFrequency(1f / WorldConfig.fromWorld(world).noise);
        }
        ChunkData chunk = createChunkData(world);
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        EndBiomeGrid grid = EndBiomeGrid.fromWorld(world);

        int mainIslandAdd = EndBiomeGrid.fromWorld(world).getBiome(xOrigin, zOrigin).equals(EndBiome.MAIN_ISLAND) ? -8 : 0;
        long biomeTime = 0;
        for (byte x = 0; x < 4; x++) {
            for (byte z = 0; z < 4; z++) {
                Interpolator interp = new Interpolator(
                        grid.getBiome(xOrigin + x * 4, zOrigin + z * 4).getGenerator().getNoise(gen, xOrigin + x * 4, zOrigin + z * 4) * 2.0f,
                        grid.getBiome(xOrigin + x * 4 + 3, zOrigin + z * 4).getGenerator().getNoise(gen,xOrigin + x * 4 + 3, zOrigin + z * 4) * 2.0f,
                        grid.getBiome(xOrigin + x * 4, zOrigin + z * 4 + 3).getGenerator().getNoise(gen,xOrigin + x * 4, zOrigin + z * 4 + 3) * 2.0f,
                        grid.getBiome(xOrigin + x * 4 + 3, zOrigin + z * 4 + 3).getGenerator().getNoise(gen,xOrigin + x * 4 + 3, zOrigin + z * 4 + 3) * 2.0f);
                for (byte x2 = 0; x2 < 4; x2++) {
                    for (byte z2 = 0; z2 < 4; z2++) {
                        double iNoise = interp.bilerp((float) x2 / 3, (float) z2 / 3);
                        int max = getMaxHeight(iNoise, world) + mainIslandAdd;
                        int min = getMinHeight(iNoise, world) + mainIslandAdd;
                        int diff = max - min;
                        long l = System.nanoTime();
                        Biome b = grid.getBiome(xOrigin + x * 4 + x2, zOrigin + z * 4 + z2);
                        biomeTime += (System.nanoTime()-l);
                        biome.setBiome(x * 4 + x2, z * 4 + z2, b.getVanillaBiome());
                        for (int y = 0; y < diff; y++) {
                            chunk.setBlock(x * 4 + x2, max - y, z * 4 + z2, b.getGenerator().getPalette().get(y, random));
                        }
                    }
                }
            }
        }
        profiler.setMeasurement("BiomeSetTime", biomeTime);
        if(totalFuture != null) totalFuture.complete();
        return chunk;
    }

    private int getMaxHeight(double iNoise, World w) {
        WorldConfig config = WorldConfig.fromWorld(w);
        return (int) (config.islandHeightMultiplierTop * (iNoise - 0.4) + config.islandHeight);
    }

    private int getMinHeight(double iNoise, World w) {
        WorldConfig config = WorldConfig.fromWorld(w);
        return (int) ((-config.islandHeightMultiplierBottom * (iNoise - 0.4) + config.islandHeight) + 1);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return main.getConfig().getBoolean("generate-end-cities", false);
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean isParallelCapable() {
        return ConfigUtil.parallel;
    }

    @NotNull
    @Override
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        if(BetterEnd.isPremium()) return Arrays.asList(new CustomStructurePopulator(), new StructurePopulator(), new TreePopulator(), new SnowPopulator());
        else return Arrays.asList(new StructurePopulator(), new TreePopulator(), new SnowPopulator());
    }

}