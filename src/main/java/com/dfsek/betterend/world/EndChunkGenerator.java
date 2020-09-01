package com.dfsek.betterend.world;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.world.EndBiomeGrid;
import org.polydev.gaea.biome.BiomeTerrain;
import com.dfsek.betterend.population.CustomStructurePopulator;
import com.dfsek.betterend.population.structures.StructurePopulator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.Interpolator;
import com.dfsek.betterend.population.TreePopulator;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.world.palette.BlockPalette;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends ChunkGenerator {
    private final BetterEnd main = BetterEnd.getInstance();

    private FastNoise gen;

    @NotNull
    @Override
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
        if (gen == null) {
            gen = new FastNoise((int) world.getSeed());
            gen.setNoiseType(FastNoise.NoiseType.SimplexFractal);
            gen.setFractalOctaves(WorldConfig.fromWorld(world).octaves);
            gen.setFrequency(1f / WorldConfig.fromWorld(world).noise);
        }
        BlockPalette p = new BlockPalette().add(Material.END_STONE, 1);
        ChunkData chunk = createChunkData(world);
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        for (byte x = 0; x < 4; x++) {
            for (byte z = 0; z < 4; z++) {
                Interpolator interp = new Interpolator(
                        getGenerator(xOrigin + x * 4, zOrigin + z * 4, world).getNoise(gen, xOrigin + x * 4, zOrigin + z * 4) * 2.0f,
                        getGenerator(xOrigin + x * 4 + 3, zOrigin + z * 4, world).getNoise(gen,xOrigin + x * 4 + 3, zOrigin + z * 4) * 2.0f,
                        getGenerator(xOrigin + x * 4, zOrigin + z * 4 + 3, world).getNoise(gen,xOrigin + x * 4, zOrigin + z * 4 + 3) * 2.0f,
                        getGenerator(xOrigin + x * 4 + 3, zOrigin + z * 4 + 3, world).getNoise(gen,xOrigin + x * 4 + 3, zOrigin + z * 4 + 3) * 2.0f);
                for (byte x2 = 0; x2 < 4; x2++) {
                    for (byte z2 = 0; z2 < 4; z2++) {
                        double iNoise = interp.bilerp((float) x2 / 3, (float) z2 / 3);
                        int diff = getMaxHeight(iNoise, world) - getMinHeight(iNoise, world);
                        for (int y = 0; y < diff; y++) {
                            chunk.setBlock(x * 4 + x2, getMaxHeight(iNoise, world) - y, z * 4 + z2, getGenerator(xOrigin + x * 4 + x2, zOrigin + z * 4 + z2, world).getPalette().get(y, random));
                        }
                    }
                }
            }
        }
        return chunk;
    }

    private BiomeTerrain getGenerator(int x, int z, World w) {
        return EndBiomeGrid.fromWorld(w).getBiome(x, z).getGenerator();
    }

    private int getMaxHeight(double iNoise, World w) {
        return (int) (WorldConfig.fromWorld(w).islandHeightMultiplierTop * (iNoise - 0.4) + 64);
    }

    private int getMinHeight(double iNoise, World w) {
        return (int) ((-WorldConfig.fromWorld(w).islandHeightMultiplierBottom * (iNoise - 0.4) + 64) + 1);
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
        if(BetterEnd.isPremium()) return Arrays.asList(new CustomStructurePopulator(), new StructurePopulator(), new TreePopulator());
        else return Arrays.asList(new StructurePopulator(), new TreePopulator());
    }

}