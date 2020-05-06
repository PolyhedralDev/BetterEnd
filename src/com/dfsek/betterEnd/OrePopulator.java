package com.dfsek.betterEnd;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class OrePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		Main main = Main.getInstance();
		int X, Y, Z;
		boolean isStone;
		if(!main.getConfig().getBoolean("aether.ores.enable-ores")) return;
		for (int i = 1; i < 20; i++) {  // Number of tries
			int decisionVal = random.nextInt(100); 
			X = random.nextInt(15);
			Z = random.nextInt(15);
			for (Y = 63; chunk.getBlock(X, Y, Z).getType() != Material.AIR && Y>0; Y--);
			if(Y > 1) {
				Y = random.nextInt(64-Y) + Y;
				if (decisionVal < 40) {  // The chance of spawning		
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.COAL_ORE);
							if (random.nextInt(100) < 85)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.COAL_ORE);
							} else isStone = false;
						}
					}
				} else if (decisionVal < 65) {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.IRON_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.IRON_ORE);
							} else isStone = false;
						}
					}
				} else if (decisionVal < 75) {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.GOLD_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.GOLD_ORE);
							} else isStone = false;
						}
					}
				} else if (decisionVal < 85) {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.REDSTONE_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.REDSTONE_ORE);
							} else isStone = false;
						}
					}
				} else if (decisionVal < 95) {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.LAPIS_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.LAPIS_ORE);
							} else isStone = false;
						}
					}
				} else if (decisionVal < 97) {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.DIAMOND_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.DIAMOND_ORE);
							} else isStone = false;
						}
					}
				} else {
					if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
						isStone = true;
						while (isStone) {
							world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(Material.EMERALD_ORE);
							if (random.nextInt(100) < 50)  {   // The chance of continuing the vein
								switch (random.nextInt(5)) {  // The direction chooser
								case 0: X++; break;
								case 1: Y++; break;
								case 2: Z++; break;
								case 3: X--; break;
								case 4: Y--; break;
								case 5: Z--; break;
								}
								isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != Material.EMERALD_ORE);
							} else isStone = false;
						}
					}
				}
			}
		}

	}

}
