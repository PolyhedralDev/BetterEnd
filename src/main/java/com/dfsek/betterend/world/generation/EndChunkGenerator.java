package com.dfsek.betterend.world.generation;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.FastNoise;
import com.dfsek.betterend.world.WorldConfig;
import com.dfsek.betterend.world.generation.biomes.Biome;
import com.dfsek.betterend.world.population.CustomStructurePopulator;
import com.dfsek.betterend.world.population.OrePopulator;
import com.dfsek.betterend.world.population.StructurePopulator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import com.dfsek.betterend.world.population.EnvironmentPopulator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends ChunkGenerator {
	private final BetterEnd main = BetterEnd.getInstance();

	private FastNoise gen;
	@NotNull
	@Override
	public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
		if(gen == null) {
			gen = new FastNoise((int) world.getSeed());
			gen.setNoiseType(FastNoise.NoiseType.SimplexFractal);
			gen.setFractalOctaves(3);
			gen.setFrequency(1f/WorldConfig.fromWorld(world).outerEndNoise);
		}
		ChunkData chunk = createChunkData(world);
		int xOrigin = chunkX << 4;
		int zOrigin = chunkZ << 4;
		for(byte x = 0; x < 16; x++) {
			for(byte z = 0; z < 16; z++) {
				double iNoise = gen.getSimplexFractal(xOrigin+x+4, zOrigin+z)*2.0f;
				for(int y = getMinHeight(iNoise, world); y < getMaxHeight(iNoise, world); y++) {
					chunk.setBlock(x, y, z, Material.END_STONE);
				}
			}
		}

		return chunk;
	}
	private int getMaxHeight(double iNoise, World w) {
		return (int) (WorldConfig.fromWorld(w).islandHeightMultiplierTop * (iNoise - ConfigUtil.landPercent) + 64);
	}
	private int getMinHeight(double iNoise, World w) {
		return (int) ((-WorldConfig.fromWorld(w).islandHeightMultiplierBottom * (iNoise - WorldConfig.fromWorld(w).landPercent) + 64) + 1);
	}
	@Override
	public boolean shouldGenerateStructures() {
		return main.getConfig().getBoolean("outer-islands.generate-end-cities", false);
	}

	@Override
	public boolean shouldGenerateDecorations() {
		return !ConfigUtil.overworld;
	}

	@Override
	public boolean shouldGenerateMobs() {
		return true;
	}

	@Override
	public boolean shouldGenerateCaves() {
		return false;
	}

	@Override
	public boolean isParallelCapable() {
		return ConfigUtil.parallel;
	}

	@NotNull
	@Override
	public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
		if(BetterEnd.isPremium()) return Arrays.asList(new CustomStructurePopulator(), new StructurePopulator(), new EnvironmentPopulator(),
				new OrePopulator());
		else return Arrays.asList(new StructurePopulator(), new EnvironmentPopulator(), new OrePopulator());
	}

}