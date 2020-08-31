package org.polydev.gaea.biome;

import com.dfsek.betterend.world.WorldConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.math.FastNoise;

public abstract class BiomeGrid<B extends BiomeTemplate> {
    private B[][] grid;
    private final FastNoise biome;
    private final FastNoise climate;
    private final World world;


    public BiomeGrid(World w) {
        this.world = w;
        this.biome = new FastNoise((int) w.getSeed());
        this.biome.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.biome.setFractalOctaves(4);
        this.biome.setFrequency((float)1/ WorldConfig.fromWorld(world).biomeSize);
        this.climate = new FastNoise((int) w.getSeed()+1);
        this.climate.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.climate.setFractalOctaves(4);
        this.climate.setFrequency((float)1/WorldConfig.fromWorld(world).climateNoise);
    }

    public BiomeGrid(int seed) {
        this.world = null;
        this.biome = new FastNoise(seed);
        this.biome.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.biome.setFractalOctaves(4);
        this.biome.setFrequency((float)1/512);
        this.climate = new FastNoise(seed +1);
        this.climate.setNoiseType(FastNoise.NoiseType.ValueFractal);
        this.climate.setFractalOctaves(4);
        this.climate.setFrequency((float)1/384);
    }

    public void setGrid(B[][] grid) {
        this.grid = grid;
    }

    /**
     * Gets the biome at a pair of coordinates.
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return Biome - Biome at the given coordinates.
     */
    public B getBiome(int x, int z) {
        float biomeNoise = biome.getValueFractal((float) x, (float) z);
        float climateNoise = climate.getValueFractal((float) x, (float) z);
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    /**
     * Gets the biome at a location.
     * @param l - The location at which to fetch the biome.
     * @return Biome - Biome at the given coordinates.
     */
    public B getBiome(Location l) {
        float biomeNoise = biome.getValueFractal((float) l.getBlockX(), (float) l.getBlockZ());
        float climateNoise = climate.getValueFractal((float) l.getBlockX(), (float) l.getBlockZ());
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 7 inclusive.
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    private static int normalize(double i) {
        i*= 17; //accounts for noise being distributed inequally
        if(i > 7.5) i = 7.5; //cuts off values too high
        if(i < -7.5) i = -7.5; //cuts off values too low
        i += 7.5; //makes it positive
        return (int) Math.floor(i);
    }
}
