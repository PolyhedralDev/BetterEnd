package com.dfsek.betterend.world;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.premium.PremiumUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.HashMap;
import java.util.Map;


public class EndBiomeGrid extends BiomeGrid {
    private static final Map<World, EndBiomeGrid> grids = new HashMap<>();
    private final WorldConfig config;

    //Grid of biomes (woah, kinda crazy i know)
    //Y = Biome type, X = "climate"
    //Holds 16x16 biomes
    private final EndBiome[][] grid = new EndBiome[][] {
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER_FOREST, EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER_FOREST, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS_FOREST},
            {EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER, EndBiome.AETHER_HIGHLANDS_BORDER, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS, EndBiome.AETHER_HIGHLANDS},
            {EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER, EndBiome.VOID_AETHER_HIGHLANDS_BORDER},
            {EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD},
            {EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.VOID, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD, EndBiome.STARFIELD},
            {EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER, EndBiome.VOID_END_BORDER},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END, EndBiome.END},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST},
            {EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_END, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST, EndBiome.SHATTERED_FOREST}};

    public EndBiomeGrid(World w) {
        super(w, 1f / WorldConfig.fromWorld(w).biomeSize, 1f / WorldConfig.fromWorld(w).climateSize, 16, 16);
        this.config = WorldConfig.fromWorld(w);
        for(EndBiome b : EndBiome.values()) {
            replaceInGrid(b, config.getBiomeReplacement(b));
        }
        try {
            if(! PremiumUtil.isPremium()) {
                replaceInGrid(EndBiome.AETHER_FOREST, EndBiome.AETHER);
                replaceInGrid(EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS);
            }
        } catch(NoClassDefFoundError e) {
            replaceInGrid(EndBiome.AETHER_FOREST, EndBiome.AETHER);
            replaceInGrid(EndBiome.AETHER_HIGHLANDS_FOREST, EndBiome.AETHER_HIGHLANDS);
        }
        super.setGrid(grid);
    }

    /**
     * Static getter for a world's BiomeGrid. Instantiates grid if it doesn't exist.
     *
     * @param w - The world in which the BiomeGrid is to be used.
     * @return BiomeGrid - The BiomeGrid linked to the world.
     */
    public static EndBiomeGrid fromWorld(World w) {
        return grids.computeIfAbsent(w, EndBiomeGrid::new);
    }

    public void replaceInGrid(EndBiome from, EndBiome to) {
        if(ConfigUtil.debug) System.out.println("Replacing " + from + " with " + to);
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(grid[i][j].equals(from)) grid[i][j] = to;
            }
        }
    }

    /**
     * Gets the biome at a pair of coordinates.
     *
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return Biome - Biome at the given coordinates.
     */
    @Override
    public EndBiome getBiome(int x, int z, GenerationPhase phase) {
        if(config.genMainIsland) {
            long ds = (long) (Math.pow(x, 2) + Math.pow(z, 2));
            if(ds < 15625) return config.getBiomeReplacement(EndBiome.MAIN_ISLAND); // 15625 = 125^2, main island width
            else if(ds < Math.pow(config.outerRadius - 10, 2))
                return config.getBiomeReplacement(EndBiome.VOID); // 980100 = 990^2, outer end edge
            else if(ds < Math.pow(config.outerRadius, 2))
                return config.getBiomeReplacement(((EndBiome) super.getBiome(x, z, phase)).getVoidBorderVariant()); // 1000000 = 1000^2, outer end beginning
        }
        return (EndBiome) super.getBiome(x, z, phase);
    }

    /**
     * Gets the biome at a location.
     *
     * @param l - The location at which to fetch the biome.
     * @return Biome - Biome at the given coordinates.
     */
    @Override
    public EndBiome getBiome(Location l) {
        return this.getBiome(l.getBlockX(), l.getBlockZ(), GenerationPhase.BASE);
    }
}
