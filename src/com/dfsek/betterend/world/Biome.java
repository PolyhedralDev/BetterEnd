package com.dfsek.betterend.world;

import org.bukkit.Location;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.EndChunkGenerator;
import com.dfsek.betterend.Main;
import com.dfsek.betterend.util.ConfigUtil;

public enum Biome {
	END,
	SHATTERED_END,
	SHATTERED_FOREST,
	AETHER,
	AETHER_HIGHLANDS,
	AETHER_FOREST,
	AETHER_HIGHLANDS_FOREST,
	VOID,
	STARFIELD;

	/**
	 * Gets the Biome at a location.
	 * @author dfsek
	 * @since 3.6.2
	 * @param Location l - The location at which to fetch the Biome.
	 * @return The biome at the specified location
	 */
	public static Biome fromLocation(Location l) {
		if(!(l.getWorld().getGenerator() instanceof EndChunkGenerator)) throw new IllegalArgumentException("Provided location is not in a BetterEnd world.");
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(l.getWorld().getSeed(), 4);
		int heatNoise = ConfigUtil.HEAT_NOISE;
		int climateNoise = ConfigUtil.CLIMATE_NOISE;
		return fromNoiseVal(biomeGenerator.noise((double) (l.getBlockX()+1000)/climateNoise, (double) (l.getBlockZ()+1000)/climateNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (l.getBlockX())/heatNoise, (double) (l.getBlockZ())/heatNoise, 0.5D, 0.5D),
				biomeGenerator.noise((double) (l.getBlockX())/ConfigUtil.BIOME_SIZE, (double) (l.getBlockZ())/ConfigUtil.BIOME_SIZE, 0.5D, 0.5D));
	}
	/**
	 * Gets the Biome from a set of coordinates.
	 * @author dfsek
	 * @since 3.6.2
	 * @param X - The X-coordinate at which to fetch the Biome.
	 * @param Z - The Z-coordinate at which to fetch the Biome.
	 * @param seed - The seed of the world in which to fetch the Biome.
	 * @return The Biome at the specified location.
	 */
	public static Biome fromCoordinates(int X, int Z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		int heatNoise = ConfigUtil.HEAT_NOISE;
		int climateNoise = ConfigUtil.CLIMATE_NOISE;
		return fromNoiseVal(biomeGenerator.noise((double) (X+1000)/climateNoise, (double) (Z+1000)/climateNoise, 0.5D, 0.5D), 
				biomeGenerator.noise((double) (X)/heatNoise, (double) (Z)/heatNoise, 0.5D, 0.5D), 
				biomeGenerator.noise((double) (X)/ConfigUtil.BIOME_SIZE, (double) (Z)/ConfigUtil.BIOME_SIZE, 0.5D, 0.5D));

	}
	/**
	 * Gets the Biome from a set of noise values<br>
	 * <b>Do not use! Uses Magic Values.</b>
	 * @author dfsek
	 * @since 3.6.2
	 * @param c - Climate Noise
	 * @param h - Heat Noise
	 * @param d - Biome Noise
	 * @return - The Biome corresponding to the given noise values.
	 */
	public static Biome fromNoiseVal(double c, double h, double d){
		if(ConfigUtil.ALL_AETHER) {
			if(h < -0.5 && c > -0.5) return Biome.AETHER_HIGHLANDS;
			else if(h < -0.5 && Main.isPremium()) return Biome.AETHER_HIGHLANDS_FOREST;
			else if(c > -0.5 || !Main.isPremium()) return Biome.AETHER;
			else return Biome.AETHER_FOREST;
		}
		if (d < -0.5 && h > 0) return Biome.SHATTERED_END;
		else if(d < -0.5 && h < 0) return Biome.SHATTERED_FOREST;
		else if (d < 0) return Biome.END;

		else if (d < 0.5 && d > 0.15 && h < -0.5) return Biome.STARFIELD;
		else if (d < 0.5) return Biome.VOID;
		else if(h < -0.5 && c > -0.5) return Biome.AETHER_HIGHLANDS;
		else if(h < -0.5 && Main.isPremium()) return Biome.AETHER_HIGHLANDS_FOREST;
		else if(c > -0.5 || !Main.isPremium()) return Biome.AETHER;
		else return Biome.AETHER_FOREST;
	}
	/**
	 * Checks whether or not the Biome is a variant of the Aether.
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is an Aether variant.
	 */
	public boolean isAether() {
		return (this.equals(Biome.AETHER) || 
				this.equals(Biome.AETHER_FOREST) || 
				this.equals(Biome.AETHER_HIGHLANDS) || 
				this.equals(Biome.AETHER_HIGHLANDS_FOREST));
	}
	/**
	 * Checks whether or not the Biome is a variant of the Highlands.
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Highlands variant.
	 */
	public boolean isHighlands() {
		return (this.equals(Biome.AETHER_HIGHLANDS) || 
				this.equals(Biome.AETHER_HIGHLANDS_FOREST));
	}
	/**
	 * Checks whether or not the Biome is a variant of the Void.
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Void variant.
	 */
	public boolean isVoid() {
		return (this.equals(Biome.VOID) ||
				this.equals(Biome.STARFIELD));
	}
	/**
	 * Checks whether or not the Biome is a variant of the Shattered End.
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Shattered End variant.
	 */
	public boolean isShattered() {
		return (this.equals(Biome.SHATTERED_END) ||
				this.equals(Biome.SHATTERED_FOREST));
	}
	/**
	 * Gets a Biome from a String containing the Biome ID.
	 * @author dfsek
	 * @since 3.6.2
	 * @param biome - The Biome ID
	 * @return The Biome matching the ID.
	 */
	public static Biome fromString(String biome) {
		switch(biome.toUpperCase()) {
		case "END": return Biome.END;
		case "SHATTERED_END": return Biome.SHATTERED_END;
		case "SHATTERED_FOREST": return Biome.SHATTERED_FOREST;
		case "AETHER": return Biome.AETHER;
		case "AETHER_HIGHLANDS": return Biome.AETHER_HIGHLANDS;
		case "AETHER_FOREST": return Biome.AETHER_FOREST;
		case "AETHER_HIGHLANDS_FOREST": return Biome.AETHER_HIGHLANDS_FOREST;
		default: throw new IllegalArgumentException("Invalid biome name \"" + biome + "\"");
		}
	}
	/**
	 * Gets the String representation of the Biome's ID.
	 * @author dfsek
	 * @since 3.6.2
	 * @return The Biome's ID as a String.
	 */
	@Override
	public String toString() {
		switch(this) {
		case END: return "END";
		case SHATTERED_END: return "SHATTERED_END";
		case SHATTERED_FOREST: return "SHATTERED_FOREST";
		case AETHER: return "AETHER";
		case AETHER_HIGHLANDS: return "AETHER_HIGHLANDS";
		case AETHER_FOREST: return "AETHER_FOREST";
		case AETHER_HIGHLANDS_FOREST: return "AETHER_HIGHLANDS_FOREST";
		default: throw new IllegalArgumentException();
		}
	}
	/**
	 * Gets the Vanilla Biome corresponding to BetterEnd Biome.
	 * @author dfsek
	 * @since 3.6.2
	 * @return Vanilla Biome representing BetterEnd Biome.
	 */
	public org.bukkit.block.Biome getVanillaBiome() {
		if(this.isAether()) return (this.isHighlands()) ? null : (ConfigUtil.OVERWORLD) ? org.bukkit.block.Biome.PLAINS : org.bukkit.block.Biome.END_HIGHLANDS;
		else if(this.isVoid()) return org.bukkit.block.Biome.END_MIDLANDS;
		else if(this.isShattered()) return org.bukkit.block.Biome.END_BARRENS;
		else return null;
	}
}
