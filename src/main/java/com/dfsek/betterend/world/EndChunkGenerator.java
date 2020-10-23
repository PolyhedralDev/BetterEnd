package com.dfsek.betterend.world;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.population.FloraPopulator;
import com.dfsek.betterend.population.OrePopulator;
import com.dfsek.betterend.population.SnowPopulator;
import com.dfsek.betterend.population.TreePopulator;
import com.dfsek.betterend.population.structures.StructurePopulator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.population.PopulationManager;
import org.polydev.gaea.world.carving.CaveCarver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EndChunkGenerator extends GaeaChunkGenerator {
    private final boolean dec;
    private static final Map<World, PopulationManager> popMap = new HashMap<>();
    private final PopulationManager popMan = new PopulationManager(BetterEnd.getInstance());
    private boolean needsLoad = true;

    public EndChunkGenerator(String world) {
        super(ChunkInterpolator.InterpolationType.TRILINEAR);
        popMan.attach(new OrePopulator());
        popMan.attach(new TreePopulator());
        popMan.attach(new SnowPopulator());
        popMan.attach(new FloraPopulator());
        popMan.attach(new StructurePopulator());
        WorldConfig config = WorldConfig.fromWorld(world);
        dec = config.genMainIsland;
    }

    public static synchronized void saveAll() {
        for(Map.Entry<World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
                if(ConfigUtil.debug) Bukkit.getLogger().info("Saved data for world " + e.getKey().getName());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public ChunkData generateBase(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, FastNoiseLite noise) {
        if(needsLoad) load(world);
        ChunkData chunk = createChunkData(world);
        WorldConfig config = WorldConfig.fromWorld(world);

        EndBiomeGrid grid = getBiomeGrid(world);
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        int mainIslandAdd = grid.getBiome(xOrigin, zOrigin, GenerationPhase.BASE).equals(EndBiome.MAIN_ISLAND) ? - 8 : 0;
        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                EndBiome b = EndBiomeGrid.fromWorld(world).getBiome(xOrigin+x, zOrigin+z, GenerationPhase.PALETTE_APPLY);
                double iNoise = super.getInterpolatedNoise(x, z);
                int max = (int) (config.islandHeightMultiplierTop * (iNoise - config.islandThreshold) + config.islandHeight) + mainIslandAdd;
                int min = (int) ((- config.islandHeightMultiplierBottom * (iNoise - config.islandThreshold) + config.islandHeight) + 1) + mainIslandAdd;
                for(int y = max; y > min; y--) {
                    chunk.setBlock(x, y, z, b.getGenerator().getPalette(y).get(max-y, x, z));
                }
            }
        }
        return config.enableCaves ? new CaveCarver(50, 12, config.islandHeight + 16, 3).carve(chunkX, chunkZ, world).merge(chunk, false) : chunk;
    }

    private void load(World w) {
        try {
            popMan.loadBlocks(w);
        } catch(IOException e) {
            if(e instanceof FileNotFoundException) {
                Bukkit.getLogger().warning("[BetterEnd] No population chunks were loaded. If this is your first time starting your server with BetterEnd v4.2+, or if you are creating a new world, this is normal.");
            } else e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        popMap.put(w, popMan);
        needsLoad = false;
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