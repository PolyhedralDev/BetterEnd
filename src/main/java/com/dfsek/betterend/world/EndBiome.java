package com.dfsek.betterend.world;

import com.dfsek.betterend.world.decor.*;
import com.dfsek.betterend.world.generators.biomes.*;
import com.dfsek.betterend.world.generators.border.AetherHighlandsBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidAetherBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidAetherHighlandsBorderGenerator;
import com.dfsek.betterend.world.generators.border.VoidEndBorderGenerator;
import com.dfsek.betterend.population.structures.EndStructure;
import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.features.BlockReplaceFeature;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.Decorator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representation of BetterEnd custom biomes.
 * 
 * @author dfsek
 * @since 3.6.2
 */
public enum EndBiome implements Biome {
	END(new EndGenerator(), new EndDecorator()),
	SHATTERED_END(new ShatteredEndGenerator(), new ShatteredEndDecorator()),
	SHATTERED_FOREST(new ShatteredEndGenerator(), new ShatteredForestDecorator()),
	AETHER(new AetherGenerator(), new AetherDecorator()),
	MAIN_ISLAND(new MainIslandGenerator(), new MainIslandDecorator()),
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
	private final Decorator<EndStructure> decorator;
	EndBiome(BiomeTerrain g, Decorator<EndStructure> d) {
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
		return(this.equals(EndBiome.AETHER)
				|| this.equals(EndBiome.AETHER_FOREST)
				|| this.equals(EndBiome.AETHER_HIGHLANDS)
				|| this.equals(EndBiome.AETHER_HIGHLANDS_FOREST)
				|| this.equals(EndBiome.VOID_AETHER_BORDER)
				|| this.equals(EndBiome.VOID_AETHER_HIGHLANDS_BORDER)
				|| this.equals(EndBiome.AETHER_HIGHLANDS_BORDER));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Highlands.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Highlands variant.
	 */
	public boolean isHighlands() {
		return(this.equals(EndBiome.AETHER_HIGHLANDS) || this.equals(EndBiome.AETHER_HIGHLANDS_FOREST));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Void.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Void variant.
	 */
	public boolean isVoid() {
		return(this.equals(EndBiome.VOID) || this.equals(EndBiome.STARFIELD));
	}

	/**
	 * Checks whether or not the Biome is a variant of the Shattered End.
	 * 
	 * @author dfsek
	 * @since 3.6.2
	 * @return Whether or not the Biome is a Shattered End variant.
	 */
	public boolean isShattered() {
		return(this.equals(EndBiome.SHATTERED_END) || this.equals(EndBiome.SHATTERED_FOREST));
	}

	/**
	 * Gets a random structure from the biome's structure collection using the given Random instance.
	 * @param r - The random instance to use.
	 * @return Structure - a random structure.
	 */
	@Override
	public EndStructure getRandomStructure(Random r) {
		return this.decorator.getStructures().get(r);
	}

	/**
	 * Gets a random tree from the biome's tree collection.
	 * @param r - The random instance to use.
	 * @return Tree - a random tree.
	 */
	@Override
	public Tree getTree(Random r) {
		return this.decorator.getTrees().get(r);
	}

	@Override
	public int getTreeDensity() {
		return decorator.getTreeDensity();
	}

	@Override
	public boolean overrideStructureChance() {
		return this.decorator.overrideStructureChance();
	}

	@Override
	public org.bukkit.block.Biome getVanillaBiome() {
		return decorator.getVanillaBiome();
	}

	public EndBiome getVoidBorderVariant() {
		switch(this) {
			case END:
			case SHATTERED_END:
			case SHATTERED_FOREST:
				return VOID_END_BORDER;
			case AETHER:
			case AETHER_FOREST:
				return VOID_AETHER_BORDER;
			case AETHER_HIGHLANDS:
			case AETHER_HIGHLANDS_FOREST:
				return VOID_AETHER_HIGHLANDS_BORDER;
			default: return this;
		}
	}

	/**
	 * Gets the generator object
	 * @return BiomeTerrain - the terrain gen object.
	 */
	@Override
	public BiomeTerrain getGenerator() {
		return this.generator;
	}

	@Override
	public List<Feature> getStructureFeatures() {
		switch (this) {
			case AETHER_HIGHLANDS_FOREST:
			case AETHER_HIGHLANDS_BORDER:
			case AETHER_HIGHLANDS:
				return Collections.singletonList(new BlockReplaceFeature(4, new ProbabilityCollection<Material>().add(Material.COBWEB, 1)));
			default: return Collections.emptyList();
		}
	}

	@Override
	public Decorator<EndStructure> getDecorator() {
		return this.decorator;
	}

	public boolean shouldGenerateSnow() {
		return decorator.shouldGenerateSnow();
	}
}
