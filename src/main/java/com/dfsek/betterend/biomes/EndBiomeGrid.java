package com.dfsek.betterend.biomes;

import com.dfsek.betterend.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

import java.util.HashMap;
import java.util.Map;


public class EndBiomeGrid extends BiomeGrid<Biome> {
    private static final Map<World, EndBiomeGrid> grids = new HashMap<>();
    //Grid of biomes (woah, kinda crazy i know)
    //Y = Biome type, X = "climate"
    //Holds 16x16 biomes
    private final Biome[][] grid = new Biome[][] {
            {Biome.AETHER_FOREST, Biome.AETHER_FOREST, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_HIGHLANDS_BORDER, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS_FOREST},
            {Biome.AETHER_FOREST, Biome.AETHER_FOREST, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_HIGHLANDS_BORDER, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS_FOREST},
            {Biome.AETHER_FOREST, Biome.AETHER_FOREST, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_HIGHLANDS_BORDER, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS_FOREST},
            {Biome.AETHER_FOREST, Biome.AETHER_FOREST, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_HIGHLANDS_BORDER, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS_FOREST},
            {Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER, Biome.VOID_AETHER_HIGHLANDS_BORDER},
            {Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD},
            {Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD},
            {Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER, Biome.VOID_END_BORDER},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST}};

    public EndBiomeGrid(World w) {
        super(w, 1f/WorldConfig.fromWorld(w).biomeSize, 1f/WorldConfig.fromWorld(w).climateSize);
        WorldConfig config = WorldConfig.fromWorld(w);
        for(Biome b : Biome.values()) {
            replaceInGrid(b, config.getBiomeReplacement(b));
        }
        super.setGrid(grid);
    }

    public void replaceInGrid(Biome from, Biome to) {
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
