package com.dfsek.betterend.biomes;

import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.biomes.generators.biomes.*;
import com.dfsek.betterend.biomes.generators.border.AetherHighlandsBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidAetherBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidAetherHighlandsBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidEndBorderGenerator;
import com.dfsek.betterend.population.structures.Structure;
import com.dfsek.betterend.population.structures.StructureProbabilityUtil;

import java.util.Random;

/**
 * Representation of BetterEnd custom biomes.
 * 
 * @author dfsek
 * @since 3.6.2
 */
public enum Biome {
	END(new EndGenerator(), StructureProbabilityUtil.END_STRUCTURES),
	SHATTERED_END(new ShatteredEndGenerator(), StructureProbabilityUtil.SHATTERED_END_STRUCTURES),
	SHATTERED_FOREST(new ShatteredEndGenerator(), StructureProbabilityUtil.SHATTERED_END_STRUCTURES),
	AETHER(new AetherGenerator(), StructureProbabilityUtil.AETHER_STRUCTURES),
	AETHER_HIGHLANDS(new AetherHighlandsGenerator(), StructureProbabilityUtil.AETHER_HIGHLAND_STRUCTURES),
	AETHER_FOREST(new AetherGenerator(), StructureProbabilityUtil.AETHER_STRUCTURES),
	AETHER_HIGHLANDS_FOREST(new AetherHighlandsGenerator(), StructureProbabilityUtil.AETHER_HIGHLAND_STRUCTURES),
	VOID(new VoidGenerator(), new ProbabilityCollection<>()),
	VOID_END_BORDER(new VoidEndBorderGenerator(), StructureProbabilityUtil.END_STRUCTURES),
	VOID_AETHER_BORDER(new VoidAetherBorderGenerator(), StructureProbabilityUtil.AETHER_STRUCTURES),
	VOID_AETHER_HIGHLANDS_BORDER(new VoidAetherHighlandsBorderGenerator(), StructureProbabilityUtil.AETHER_HIGHLAND_STRUCTURES),
	AETHER_HIGHLANDS_BORDER(new AetherHighlandsBorderGenerator(), StructureProbabilityUtil.AETHER_HIGHLAND_STRUCTURES),
	STARFIELD(new VoidGenerator(), StructureProbabilityUtil.STARFIELD_STRUCTURES);

	private final BiomeTerrain generator;
	private final ProbabilityCollection<Structure> structures;
	Biome(BiomeTerrain g, ProbabilityCollection<Structure> s) {
		this.generator = g;
		this.structures = s;
	}

	/**
	 * Checks whether or not the Biome is a variant of the Aether.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is an Aether variant.
	 */
	public boolean isAether() {
		return(this.equals(Biome.AETHER)
				|| this.equals(Biome.AETHER_FOREST)
				|| this.equals(Biome.AETHER_HIGHLANDS)
				|| this.equals(Biome.AETHER_HIGHLANDS_FOREST)
				|| this.equals(Biome.VOID_AETHER_BORDER)
				|| this.equals(Biome.VOID_AETHER_HIGHLANDS_BORDER)
				|| this.equals(Biome.AETHER_HIGHLANDS_BORDER));
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
	 * Gets a random structure from the biome's structure collection using the given Random instance.
	 * @param r - The random instance to use.
	 * @return Structure - a random structure.
	 */
	public Structure getRandomStructure(Random r) {
		return this.structures.get(r);
	}

	/**
	 * Gets the generator object
	 * @return BiomeTerrain - the terrain gen object.
	 */
	public BiomeTerrain getGenerator() {
		return this.generator;
	}
}
