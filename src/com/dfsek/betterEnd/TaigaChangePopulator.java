package com.dfsek.betterEnd;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class TaigaChangePopulator extends BlockPopulator {
	Main main = Main.getInstance();
	int biomeSize = main.getConfig().getInt("outer-islands.biome-size");
	boolean allAether = main.getConfig().getBoolean("all-aether", false);
	int heatNoise = main.getConfig().getInt("outer-islands.heat-noise");
	@SuppressWarnings("deprecation")
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		for(int X = 0; X < 16; X++) {
			for(int Z = 0; Z < 16; Z++) {
				int Y;
				for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() == Material.AIR ||
						chunk.getBlock(X, Y, Z).getType() == Material.WHITE_STAINED_GLASS ||
								chunk.getBlock(X, Y, Z).getType() == Material.STONE_SLAB ||
								chunk.getBlock(X, Y, Z).getType() == Material.COBBLESTONE_WALL ||
								chunk.getBlock(X, Y, Z).getType() == Material.IRON_BARS ||
								chunk.getBlock(X, Y, Z).isPassable()) && Y>0; Y--);
				
				double biomeNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/biomeSize, (double) (chunk.getZ()*16+Z)/biomeSize, 0.5D, 0.5D);
				double heatNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/heatNoise, (double) (chunk.getZ()*16+Z)/heatNoise, 0.5D, 0.5D);
				if(heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || allAether)) world.setBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, Biome.TAIGA);
				int snow = random.nextInt(100);
				if(snow < -100*(heatNoiseLvl+0.5) && Y > 1 && heatNoiseLvl < -0.5 && heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || allAether)) {
					world.getBlockAt(new Location(world, chunk.getX()*16+X, Y+1, chunk.getZ()*16+Z)).setType(Material.SNOW);
					Material upMaterial = world.getBlockAt(new Location(world, chunk.getX()*16+X, Y+2, chunk.getZ()*16+Z)).getType();
					if(upMaterial == Material.TALL_GRASS || upMaterial == Material.COBBLESTONE_WALL) {
						world.getBlockAt(new Location(world, chunk.getX()*16+X, Y+2, chunk.getZ()*16+Z)).setType(Material.AIR);
					}
				}
				
			}
		}
		if(random.nextInt(100) < 20) {
			int Y;
			int X = random.nextInt(16);
			int Z = random.nextInt(16);
			for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && 
					chunk.getBlock(X, Y, Z).getType() != Material.GRAVEL &&
					chunk.getBlock(X, Y, Z).getType() != Material.PODZOL &&
					chunk.getBlock(X, Y, Z).getType() != Material.COARSE_DIRT) && Y>0; Y--);
			double biomeNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/biomeSize, (double) (chunk.getZ()*16+Z)/biomeSize, 0.5D, 0.5D);
			double heatNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/heatNoise, (double) (chunk.getZ()*16+Z)/heatNoise, 0.5D, 0.5D);
			if(Y > 1 && heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || allAether)) world.getBlockAt(chunk.getX()*16+X, Y+1, chunk.getZ()*16+Z).setType(Material.COBBLESTONE);
		}
	}

}
