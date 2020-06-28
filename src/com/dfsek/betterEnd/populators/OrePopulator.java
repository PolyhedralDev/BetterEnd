package com.dfsek.betterend.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.Ore;

public class OrePopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int X;
		int Y;
		int Z;
		if(!ConfigUtil.DO_AETHER_ORES) return;
		for (int i = 1; i < ConfigUtil.AETHER_ORE_CHANCE; i++) {  // Number of tries
			Ore ore = (Ore) Util.chooseOnWeight(new Ore[] {new Ore(Material.COAL_ORE, 85), 
					new Ore(Material.IRON_ORE, 50), 
					new Ore(Material.GOLD_ORE, 50),
					new Ore(Material.REDSTONE_ORE, 65),
					new Ore(Material.LAPIS_ORE, 75),
					new Ore(Material.DIAMOND_ORE, 50),
					new Ore(Material.EMERALD_ORE, 40)}, ConfigUtil.ORE_CHANCES);
			X = random.nextInt(15);
			Z = random.nextInt(15);
			for (Y = 63; chunk.getBlock(X, Y, Z).getType() != Material.AIR && Y>0; Y--);
			if(Y > 1) {
				Y = random.nextInt(64-Y) + Y;
					doVein(world, chunk, random, new int[] {X, Y, Z}, ore.getType(), ore.getContChance());
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