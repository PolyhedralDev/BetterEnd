package com.dfsek.betterend.world;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.population.FaunaPopulator;
import com.dfsek.betterend.population.OrePopulator;
import com.dfsek.betterend.population.SnowPopulator;
import com.dfsek.betterend.population.TreePopulator;
import com.dfsek.betterend.population.structures.StructurePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.population.PopulationManager;
import org.polydev.gaea.world.carving.CaveCarver;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends GaeaChunkGenerator {
    private final boolean dec;
    private final PopulationManager popMan = new PopulationManager();

    public EndChunkGenerator(String world) {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        popMan.attach(new OrePopulator());
        popMan.attach(new StructurePopulator());
        popMan.attach(new TreePopulator());
        popMan.attach(new SnowPopulator());
        popMan.attach(new FaunaPopulator());
        WorldConfig config = WorldConfig.fromWorld(world);
        dec = config.genMainIsland;
    }

    @Override
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, FastNoise noise) {
        ChunkData chunk = createChunkData(world);
        WorldConfig config = WorldConfig.fromWorld(world);

        EndBiomeGrid grid = getBiomeGrid(world);
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        int mainIslandAdd = grid.getBiome(xOrigin, zOrigin).equals(EndBiome.MAIN_ISLAND) ? - 8 : 0;
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                double iNoise = super.getInterpolatedNoise(x, z);
                int max = (int) (config.islandHeightMultiplierTop * (iNoise - 0.4) + config.islandHeight) + mainIslandAdd;
                int min = (int) ((- config.islandHeightMultiplierBottom * (iNoise - 0.4) + config.islandHeight) + 1) + mainIslandAdd;
                for(int y = min; y < max; y++) {
                    chunk.setBlock(x, y, z, Material.STONE);
                }
            }
        }
        return config.enableCaves ? new CaveCarver(50, 12, config.islandHeight + 16, 3).carve(chunkX, chunkZ, world).merge(chunk, false) : chunk;
    }

    @Override
    public int getNoiseOctaves(World w) {
        return WorldConfig.fromWorld(w).octaves;
    }

    @Override
    public float getNoiseFrequency(World w) {
        return 1f / WorldConfig.fromWorld(w).noise;
    }

    @Override
    public List<GenerationPopulator> getGenerationPopulators(World w) {
        return Collections.emptyList();
    }

    @Override
    public EndBiomeGrid getBiomeGrid(World w) {
        return EndBiomeGrid.fromWorld(w);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return ConfigUtil.endCities;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return dec;
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
        return Collections.singletonList(popMan);
    }

}