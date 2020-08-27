package com.dfsek.betterend.biomes;

import com.dfsek.betterend.generation.FastNoise;
import com.dfsek.betterend.world.WorldConfig;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;


public class BiomeGrid {
    private static final Map<World, BiomeGrid> grids = new HashMap<>();
    //Grid of biomes (woah, kinda crazy i know)
    //Y = Biome type, X = "climate"
    //Holds 8x8 biomes
    private final Biome[][] grid = new Biome[][] {
            {Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_FOREST, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS},
            {Biome.AETHER, Biome.AETHER, Biome.AETHER, Biome.AETHER_FOREST, Biome.AETHER_HIGHLANDS_FOREST, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS, Biome.AETHER_HIGHLANDS},
            {Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD},
            {Biome.VOID, Biome.VOID, Biome.VOID, Biome.VOID, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD, Biome.STARFIELD},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END, Biome.END},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST},
            {Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_END, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST, Biome.SHATTERED_FOREST}};
    FastNoise biome;
    FastNoise climate;
    World world;

    /**
     * Instantiates a BiomeGrid linked to a world.
     * @param w - The world in which the BiomeGrid is to be used.
     */
    private BiomeGrid(World w) {
        this.world = w;
        this.biome = new FastNoise((int) w.getSeed());
        this.biome.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.biome.setFractalOctaves(4);
        this.biome.setFrequency((float)1/WorldConfig.fromWorld(world).biomeSize);
        this.climate = new FastNoise((int) w.getSeed()+1);
        this.climate.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.climate.setFractalOctaves(4);
        this.climate.setFrequency((float)1/WorldConfig.fromWorld(world).climateNoise);
    }

    /**
     * Constructor for tests. Not for production.
     * Sets seed to 5, because 5 is a cool number B)
     */
    public BiomeGrid() {
        this.biome = new FastNoise(5);
        this.biome.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.biome.setFractalOctaves(4);
        this.biome.setFrequency((float)1/512);
        this.climate = new FastNoise(6);
        this.climate.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.climate.setFractalOctaves(4);
        this.climate.setFrequency((float)1/384);
    }

    /**
     * Constructor for tests. Not for production.
     */
    public BiomeGrid(int seed) {
        this.biome = new FastNoise(seed);
        this.biome.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.biome.setFractalOctaves(4);
        this.biome.setFrequency((float)1/512);
        this.climate = new FastNoise(seed+1);
        this.climate.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.climate.setFractalOctaves(4);
        this.climate.setFrequency((float)1/384);
    }

    /**
     * Gets the biome at a pair of coordinates.
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return Biome - Biome at the given coordinates.
     */
    public Biome getBiome(int x, int z) {
        float biomeNoise = biome.getValueFractal((float) x, (float) z);
        float climateNoise = climate.getValueFractal((float) x, (float) z);
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    /**
     * Gets the biome at a location.
     * @param l - The location at which to fetch the biome.
     * @return Biome - Biome at the given coordinates.
     */
    public Biome getBiome(Location l) {
        float biomeNoise = biome.getValueFractal((float) l.getBlockX(), (float) l.getBlockZ());
        float climateNoise = climate.getValueFractal((float) l.getBlockX(), (float) l.getBlockZ());
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    /**
     * Static getter for a world's BiomeGrid. Instantiates grid if it doesn't exist.
     * @param w - The world in which the BiomeGrid is to be used.
     * @return BiomeGrid - The BiomeGrid linked to the world.
     */
    public static BiomeGrid fromWorld(World w) {
        if(grids.containsKey(w)) return grids.get(w);
        BiomeGrid g = new BiomeGrid(w);
        grids.put(w, g);
        return g;
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 7 inclusive.
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    private static int normalize(double i) {
        i*= 9; //accounts for noise being distributed inequally
        if(i > 3.5) i = 3.5; //cuts off values too high
        if(i < -3.5) i = -3.5; //cuts off values too low
        i += 3.5; //makes it positive
        return (int) Math.floor(i);
    }
}
