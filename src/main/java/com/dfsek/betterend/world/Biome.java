package com.dfsek.betterend.world;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.world.terrain.BiomeGenerator;
import com.dfsek.betterend.world.terrain.biomes.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.terrain.EndChunkGenerator;

import java.lang.reflect.InvocationTargetException;

/**
 * Representation of BetterEnd custom biomes.
 * 
 * @author dfsek
 * @since 3.6.2
 */
public enum Biome {
	END, SHATTERED_END, SHATTERED_FOREST, AETHER, AETHER_HIGHLANDS, AETHER_FOREST, AETHER_HIGHLANDS_FOREST, VOID, STARFIELD;


	/**
	 * Gets the Biome at a location.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @param l
	 *          - The location at which to fetch the Biome.
	 * @return The biome at the specified location
	 */
	public static Biome fromLocation(Location l) {
		if(!(l.getWorld().getGenerator() instanceof EndChunkGenerator)) throw new IllegalArgumentException("Provided location is not in a BetterEnd world.");
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l.getWorld().getSeed(), 4);
		return fromNoiseVal(
				biomeGenerator.noise((double) (l.getBlockX() + 1000) / ConfigUtil.climateNoise, (double) (l.getBlockZ() + 1000) / ConfigUtil.climateNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (l.getBlockX()) / ConfigUtil.heatNoise, (double) (l.getBlockZ()) / ConfigUtil.heatNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (l.getBlockX()) / ConfigUtil.biomeSize, (double) (l.getBlockZ()) / ConfigUtil.biomeSize, 0.5D, 0.5D));
	}

	/**
	 * Gets the Biome from a set of coordinates.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @param x
	 *          - The X-coordinate at which to fetch the Biome.
	 * @param z
	 *          - The Z-coordinate at which to fetch the Biome.
	 * @param seed
	 *          - The seed of the world in which to fetch the Biome.
	 * @return The Biome at the specified location.
	 */
	public static Biome fromCoordinates(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		int heatNoise = ConfigUtil.heatNoise;
		int climateNoise = ConfigUtil.climateNoise;
		return fromNoiseVal(biomeGenerator.noise((double) (x + 1000) / climateNoise, (double) (z + 1000) / climateNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (x) / heatNoise, (double) (z) / heatNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D));

	}

	/**
	 * Gets the Biome from a set of noise values<br>
	 * <b>Do not use! Uses Magic Values.</b>
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @param c
	 *          - Climate Noise
	 * @param h
	 *          - Heat Noise
	 * @param d
	 *          - Biome Noise
	 * @return - The Biome corresponding to the given noise values.
	 */
	public static Biome fromNoiseVal(double c, double h, double d) {
		if(ConfigUtil.allAether) {
			if(h < -0.5 && c > -0.5) return Biome.AETHER_HIGHLANDS;
			else if(h < -0.5 && BetterEnd.isPremium()) return Biome.AETHER_HIGHLANDS_FOREST;
			else if(c > -0.5 || !BetterEnd.isPremium()) return Biome.AETHER;
			else return Biome.AETHER_FOREST;
		}
		if(d < -0.5 && h > 0) return Biome.SHATTERED_END;
		else if(d < -0.5 && h < 0) return Biome.SHATTERED_FOREST;
		else if(d < 0) return Biome.END;

		else if(d < 0.5 && d > 0.15 && h < -0.5) return Biome.STARFIELD;
		else if(d < 0.5) return Biome.VOID;
		else if(h < -0.5 && c > -0.5) return Biome.AETHER_HIGHLANDS;
		else if(h < -0.5 && BetterEnd.isPremium()) return Biome.AETHER_HIGHLANDS_FOREST;
		else if(c > -0.5 || !BetterEnd.isPremium()) return Biome.AETHER;
		else return Biome.AETHER_FOREST;
	}

	/**
	 * Checks whether or not the Biome is a variant of the Aether.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is an Aether variant.
	 */
	public boolean isAether() {
		return(this.equals(Biome.AETHER) || this.equals(Biome.AETHER_FOREST) || this.equals(Biome.AETHER_HIGHLANDS) || this.equals(Biome.AETHER_HIGHLANDS_FOREST));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Highlands.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Highlands variant.
	 */
	public boolean isHighlands() {
		return(this.equals(Biome.AETHER_HIGHLANDS) || this.equals(Biome.AETHER_HIGHLANDS_FOREST));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Void.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Void variant.
	 */
	public boolean isVoid() {
		return(this.equals(Biome.VOID) || this.equals(Biome.STARFIELD));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Shattered End.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Shattered End variant.
	 */
	public boolean isShattered() {
		return(this.equals(Biome.SHATTERED_END) || this.equals(Biome.SHATTERED_FOREST));
	}

	/**
	 * Gets a Biome from a String containing the Biome ID.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @param biome
	 *          - The Biome ID
	 * @return The Biome matching the ID.
	 */
	public static Biome fromString(String biome) {
		switch(biome.toUpperCase()) {
			case "END":
				return Biome.END;
			case "SHATTERED_END":
				return Biome.SHATTERED_END;
			case "SHATTERED_FOREST":
				return Biome.SHATTERED_FOREST;
			case "AETHER":
				return Biome.AETHER;
			case "AETHER_HIGHLANDS":
				return Biome.AETHER_HIGHLANDS;
			case "AETHER_FOREST":
				return Biome.AETHER_FOREST;
			case "AETHER_HIGHLANDS_FOREST":
				return Biome.AETHER_HIGHLANDS_FOREST;
			case "VOID":
				return Biome.VOID;
			case "STARFIELD":
				return Biome.STARFIELD;
			default:
				throw new IllegalArgumentException("Invalid biome name \"" + biome + "\"");
		}
	}

	/**
	 * Gets the String representation of the Biome's ID.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return The Biome's ID as a String.
	 */
	@Override
	public String toString() {
		switch(this) {
			case END:
				return "END";
			case SHATTERED_END:
				return "SHATTERED_END";
			case SHATTERED_FOREST:
				return "SHATTERED_FOREST";
			case AETHER:
				return "AETHER";
			case AETHER_HIGHLANDS:
				return "AETHER_HIGHLANDS";
			case AETHER_FOREST:
				return "AETHER_FOREST";
			case AETHER_HIGHLANDS_FOREST:
				return "AETHER_HIGHLANDS_FOREST";
			case VOID:
				return "VOID";
			case STARFIELD:
				return "STARFIELD";
			default:
				throw new IllegalArgumentException();
		}
	}

	public BiomeGenerator getGenerator(World world) {
		switch (this) {
			case AETHER:
			case AETHER_FOREST:
				return new AetherGenerator(world);
			case AETHER_HIGHLANDS:
			case AETHER_HIGHLANDS_FOREST:
				return new AetherHighlandsGenerator(world);
			case SHATTERED_END:
			case SHATTERED_FOREST:
				return new ShatteredEndGenerator(world);
			case VOID:
			case STARFIELD:
				return new VoidGenerator(world);
			case END:
				return new EndGenerator(world);
		}
		return null;
	}
	public static double getVoidLevel(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		if(d < 0 || d > 0.5) return 0;
		return -8D*Math.abs(d-0.25)+2D;
	}
	public static double getShatteredLevel(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		if(d > -0.5) return 0;
		return (-2D*d -1D > 1) ? 1 : -2D*d -1D;
	}
	public static boolean isAetherVoid(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		return d > 0.4;
	}
}
