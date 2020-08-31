package com.dfsek.betterend.biomes;

import com.dfsek.betterend.biomes.decor.*;
import com.dfsek.betterend.biomes.generators.biomes.*;
import com.dfsek.betterend.biomes.generators.border.AetherHighlandsBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidAetherBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidAetherHighlandsBorderGenerator;
import com.dfsek.betterend.biomes.generators.border.VoidEndBorderGenerator;
import com.dfsek.betterend.population.structures.Structure;
import org.polydev.gaea.terrain2.BiomeTerrain;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Decorator;

import java.util.Random;

/**
 * Representation of BetterEnd custom biomes.
 * 
 * @author dfsek
 * @since 3.6.2
 */
public enum Biome {
	END(new EndGenerator(), new EndDecorator()),
	SHATTERED_END(new ShatteredEndGenerator(), new ShatteredEndDecorator()),
	SHATTERED_FOREST(new ShatteredEndGenerator(), new ShatteredForestDecorator()),
	AETHER(new AetherGenerator(), new AetherDecorator()),
	AETHER_HIGHLANDS(new AetherHighlandsGenerator(), new AetherHighlandsDecorator()),
	AETHER_FOREST(new AetherGenerator(), new AetherForestDecorator()),
	AETHER_HIGHLANDS_FOREST(new AetherHighlandsGenerator(), new AetherHighlandsForestDecorator()),
	VOID(new VoidGenerator(), new VoidDecorator()),
	VOID_END_BORDER(new VoidEndBorderGenerator(), new EndDecorator()),
	VOID_AETHER_BORDER(new VoidAetherBorderGenerator(), new AetherDecorator()),
	VOID_AETHER_HIGHLANDS_BORDER(new VoidAetherHighlandsBorderGenerator(), new AetherHighlandsDecorator()),
	AETHER_HIGHLANDS_BORDER(new AetherHighlandsBorderGenerator(), new AetherHighlandsDecorator()),
	STARFIELD(new VoidGenerator(), new StarfieldDecorator());

	private final BiomeTerrain generator;
	private final Decorator<Structure> decorator;
	Biome(BiomeTerrain g, Decorator<Structure> d) {
		this.generator = g;
		this.decorator = d;
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
	 * Gets a random structure from the biome's structure collection using the given Random instance.
	 * @param r - The random instance to use.
	 * @return Structure - a random structure.
	 */
	public Structure getRandomStructure(Random r) {
		return this.decorator.getStructures().get(r);
	}

	/**
	 * Gets a random tree from the biome's tree collection.
	 * @param r - The random instance to use.
	 * @return Tree - a random tree.
	 */
	public Tree getTree(Random r) {
		return this.decorator.getTrees().get(r);
	}
	
	public int getTreeDensity() {
		return decorator.getTreeDensity();
	}

	/**
	 * Gets the generator object
	 * @return BiomeTerrain - the terrain gen object.
	 */
	public BiomeTerrain getGenerator() {
		return this.generator;
	}
}
