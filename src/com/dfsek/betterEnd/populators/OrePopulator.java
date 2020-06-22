package com.dfsek.betterend.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.dfsek.betterend.ConfigUtil;

public class OrePopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk chunk) {

		int X;
		int Y;
		int Z;
		if(!ConfigUtil.DO_AETHER_ORES) return;
		for (int i = 1; i < ConfigUtil.AETHER_ORE_CHANCE; i++) {  // Number of tries
			int decisionVal = random.nextInt(100); 
			X = random.nextInt(15);
			Z = random.nextInt(15);
			for (Y = 63; chunk.getBlock(X, Y, Z).getType() != Material.AIR && Y>0; Y--);
			if(Y > 1) {
				Y = random.nextInt(64-Y) + Y;
				if (decisionVal < 40) {  // The chance of spawning	
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.COAL_ORE, 85);
				} else if (decisionVal < 65) {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.IRON_ORE, 50);
				} else if (decisionVal < 75) {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.GOLD_ORE, 50);
				} else if (decisionVal < 85) {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.REDSTONE_ORE, 65);
				} else if (decisionVal < 95) {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.LAPIS_ORE, 75);
				} else if (decisionVal < 97) {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.DIAMOND_ORE, 50);
				} else {
					doVein(world, chunk, random, new int[] {X, Y, Z}, Material.EMERALD_ORE, 40);
				}
			}
		}

	}
	private void doVein(World world, Chunk chunk, Random random, int[] coords, Material ore, int continueChance) {
		int X = coords[0];
		int Y = coords[1];
		int Z = coords[2];
		if (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) {
			boolean isStone = true;
			while(isStone) {
				world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).setType(ore);
				if(random.nextInt(100) < continueChance)  {
					switch (random.nextInt(6)) {
					case 0: X++; break;
					case 1: Y++; break;
					case 2: Z++; break;
					case 3: X--; break;
					case 4: Y--; break;
					case 5: Z--; break;
					default:
					}
					isStone = (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() == Material.STONE) && (world.getBlockAt(X+chunk.getX()*16, Y, Z+chunk.getZ()*16).getType() != ore);
				} else isStone = false;
			}
		}
	}

}