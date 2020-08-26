package com.dfsek.betterend.world.generation.biomes;

import com.dfsek.betterend.world.generation.biomes.generators.*;
import org.bukkit.World;

/**
 * Representation of BetterEnd custom biomes.
 * 
 * @author dfsek
 * @since 3.6.2
 */
public enum Biome {
	END, SHATTERED_END, SHATTERED_FOREST, AETHER, AETHER_HIGHLANDS, AETHER_FOREST, AETHER_HIGHLANDS_FOREST, VOID, STARFIELD;

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
}
