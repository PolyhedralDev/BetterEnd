package com.dfsek.betterEnd;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class GrassPopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		for (int X = chunk.getX()*16; X < chunk.getX()*16+16; X++)
			for (int Z = chunk.getZ()*16; Z < chunk.getZ()*16+16; Z++) {
				if(Main.getBiome(X, Z, world.getSeed()).equalsIgnoreCase("AETHER")) {
					if(random.nextInt(100) < 55) {
						int Y;
						for (Y = world.getMaxHeight()-1; Y>0 && chunk.getBlock(X-chunk.getX()*16, Y, Z-chunk.getZ()*16).getType() != Material.GRASS_BLOCK; Y--);
						if(Y>0) {
							if(world.getBlockAt(X, Y, Z).getType() == Material.GRASS_BLOCK && world.getBlockAt(X, Y+1, Z).getType() == Material.AIR) {
								double grassType = random.nextDouble()*100;
								if(grassType < 80) {
									world.getBlockAt(X, Y+1, Z).setType(Material.GRASS);
								} else if(grassType < 85) {
									world.getBlockAt(X, Y+1, Z).setType(Material.TALL_GRASS);
									world.getBlockAt(X, Y+2, Z).setType(Material.TALL_GRASS);
								} else if(grassType < 88) {
									world.getBlockAt(X, Y+1, Z).setType(Material.LILY_OF_THE_VALLEY);
								} else if(grassType < 91)  {
									world.getBlockAt(X, Y+1, Z).setType(Material.FERN);
								} else if(grassType < 96) {
									world.getBlockAt(X, Y+1, Z).setType(Material.AZURE_BLUET);
								} else if(grassType < 99.75) {
									world.getBlockAt(X, Y+1, Z).setType(Material.BLUE_ORCHID);
								} else {
									world.getBlockAt(X, Y+1, Z).setType(Material.WITHER_ROSE);	
								}
							}
						}
					}
				}
			}

	}
}
