package com.dfsek.betterend.generation;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.WorldConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends ChunkGenerator {
	private final BetterEnd main = BetterEnd.getInstance();
	private BlockPalette p;

	private FastNoise gen;
	@NotNull
	@Override
	public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
		if(gen == null) {
			gen = new FastNoise((int) world.getSeed());
			gen.setNoiseType(FastNoise.NoiseType.SimplexFractal);
			gen.setFractalOctaves(4);
			gen.setFrequency(1f/WorldConfig.fromWorld(world).outerEndNoise);
		}
		if(p == null) {
			p = new BlockPalette(random).add(Material.GRASS_BLOCK, 1).add(Material.DIRT, 2).add(Material.STONE, 1);
		}
		ChunkData chunk = createChunkData(world);
		int xOrigin = chunkX << 4;
		int zOrigin = chunkZ << 4;
		for(byte x = 0; x < 16; x++) {
			for(byte z = 0; z < 16; z++) {
				double iNoise = gen.getSimplexFractal(xOrigin+x+4, zOrigin+z)*2.0f;
				int diff = getMaxHeight(iNoise, world) - getMinHeight(iNoise, world);
				for(int y = 0; y < diff; y++) {
					chunk.setBlock(x, getMaxHeight(iNoise, world)-y, z, p.get(y));
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
		return new ArrayList<>();
		//if(BetterEnd.isPremium()) return Arrays.asList(new CustomStructurePopulator(), new StructurePopulator(), new EnvironmentPopulator(),
				//new OrePopulator());
		//else return Arrays.asList(new StructurePopulator(), new EnvironmentPopulator(), new OrePopulator());
	}

}