package com.dfsek.betterEnd;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {
	public void populate(World world, Random random, Chunk chunk) {
		Main main = Main.getInstance();
		int min = main.getConfig().getInt("trees.min-per-chunk");
		int max = main.getConfig().getInt("trees.max-per-chunk");
		int obMax = main.getConfig().getInt("trees.obsidian-pillars.max-height");
		int obMin = main.getConfig().getInt("trees.obsidian-pillars.min-height");
		int amount = random.nextInt(max-min)+min;  // Amount of trees
		for (int i = 1; i < amount; i++) {
			int X = random.nextInt(15);
			int Z = random.nextInt(15);
			int Y;
			for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR && Y>0; Y--); // Find the highest block of the (X,Z) coordinate chosen.
			if (Y > 1 && Y < 255) {
				Location blockLocation = chunk.getBlock(X, Y, Z).getLocation();
				switch(Main.getBiome(blockLocation.getBlockX(), blockLocation.getBlockZ(), world.getSeed())){
				case "END":
					if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 8) {
						world.generateTree(blockLocation.add(0,1,0), TreeType.CHORUS_PLANT);
					}
					break;
				case "AETHER":
					for (int j = 0; j < 3; j++) {
						X = random.nextInt(15);
						Z = random.nextInt(15);
						for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR && Y>0; Y--);
						int tree = random.nextInt(100);
						if(blockLocation.getBlock().getType() == Material.GRASS_BLOCK || blockLocation.subtract(0, 1, 0).getBlock().getType() == Material.GRASS_BLOCK) {
							if(tree < 45) {
								world.generateTree(blockLocation, TreeType.TREE);
							}  else if(tree < 93) {
								world.generateTree(blockLocation, TreeType.JUNGLE_BUSH);
							} else {
								world.generateTree(blockLocation, TreeType.BIG_TREE);
							}
						}
					}
					break;
				case "SHATTERED_END":
					if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 7) {
						int[] upBound = {random.nextInt(obMax-obMin)+obMin, random.nextInt(obMax-obMin)+obMin, random.nextInt(obMax-obMin)+obMin, random.nextInt(obMax-obMin)+obMin};
						int highBound = 0;
						for(int j = 1; j < upBound.length; j++) {
							if(upBound[j-1] < upBound[j]) highBound = j;
						}
						for(int j = -upBound[0]; j < upBound[0]; j++) {
							world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z).setType(Material.OBSIDIAN);
						}
						for(int j = -upBound[1]; j < upBound[1]; j++) {
							world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z).setType(Material.OBSIDIAN);
						}
						for(int j = -upBound[2]; j < upBound[2]; j++) {
							world.getBlockAt((chunk.getX()*16)+X, Y+j, (chunk.getZ()*16)+Z+1).setType(Material.OBSIDIAN);
						}
						for(int j = -upBound[3]; j < upBound[3]; j++) {
							world.getBlockAt((chunk.getX()*16)+X+1, Y+j, (chunk.getZ()*16)+Z+1).setType(Material.OBSIDIAN);
						}
						if(random.nextInt(100) < 10) {
							switch(highBound) {
							case 0:
								world.spawnEntity(new Location(world, (chunk.getX()*16)+X+0.5, upBound[0]+Y, (chunk.getZ()*16)+Z+0.5), EntityType.ENDER_CRYSTAL);
								break;
							case 1:
								world.spawnEntity(new Location(world, (chunk.getX()*16)+X+1.5, upBound[1]+Y, (chunk.getZ()*16)+Z+0.5), EntityType.ENDER_CRYSTAL);
								break;
							case 2:
								world.spawnEntity(new Location(world, (chunk.getX()*16)+X+0.5, upBound[2]+Y, (chunk.getZ()*16)+Z+1.5), EntityType.ENDER_CRYSTAL);
								break;
							case 3:
								world.spawnEntity(new Location(world, (chunk.getX()*16)+X+1.5, upBound[3]+Y, (chunk.getZ()*16)+Z+1.5), EntityType.ENDER_CRYSTAL);
								break;
							}

						}
					}
					break;
				}
				// The tree type can be changed if you want.
			}
		}
	}

} 
