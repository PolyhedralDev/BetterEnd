package com.dfsek.betterend.world.terrain;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.populators.CustomStructurePopulator;
import com.dfsek.betterend.world.populators.OrePopulator;
import com.dfsek.betterend.world.populators.StructurePopulator;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import com.dfsek.betterend.world.populators.EnvironmentPopulator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EndChunkGenerator extends ChunkGenerator {
	private final BetterEnd main = BetterEnd.getInstance();

	@NotNull
	@Override
	public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		int xOrigin = chunkX << 4;
		int zOrigin = chunkZ << 4;
		for(byte x = 0; x < 16; x++) {
			for(byte z = 0; z < 16; z++) {
				chunk = Biome.fromCoordinates(xOrigin+x, zOrigin+z, world).getGenerator(world).generateSlice(x, z, chunkX, chunkZ).insert(chunk);
			}
		}

		return chunk;
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