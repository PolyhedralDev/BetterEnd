package com.dfsek.betterend.world;

import com.dfsek.betterend.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

import java.util.HashMap;
import java.util.Map;


public class EndBiomeGrid extends BiomeGrid<EndBiome> {
    private static final Map<World, EndBiomeGrid> grids = new HashMap<>();
    //Grid of biomes (woah, kinda crazy i know)
    //Y = Biome type, X = "climate"
    //Holds 16x16 biomes
    private final EndBiome[][] grid = new EndBiome[][] {
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER},
            {EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD},
            {EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD},
            {EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST}};

    public EndBiomeGrid(World w) {
        super(w, 1f/WorldConfig.fromWorld(w).biomeSize, 1f/WorldConfig.fromWorld(w).climateSize);
        WorldConfig config = WorldConfig.fromWorld(w);
        for(EndBiome b : EndBiome.values()) {
            replaceInGrid(b, config.getBiomeReplacement(b));
        }
        super.setGrid(grid);
    }

    public void replaceInGrid(EndBiome from, EndBiome to) {
        System.out.println("Replacing " + from + " with " + to);
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(grid[i][j].equals(from)) grid[i][j] = to;
            }
        }
    }

    public EndBiomeGrid(int seed) {
        super(seed);
        super.setGrid(grid);
    }

    /**
     * Static getter for a world's BiomeGrid. Instantiates grid if it doesn't exist.
     * @param w - The world in which the BiomeGrid is to be used.
     * @return BiomeGrid - The BiomeGrid linked to the world.
     */
    public static EndBiomeGrid fromWorld(World w) {
        if(grids.containsKey(w)) return grids.get(w);
        EndBiomeGrid g = new EndBiomeGrid(w);
        grids.put(w, g);
        return g;
    }
}
