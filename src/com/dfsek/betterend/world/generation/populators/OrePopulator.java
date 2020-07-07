package com.dfsek.betterend.world.generation.populators;

import java.util.Random;

import com.dfsek.betterend.ProbabilityCollection;
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
		int x;
		int y;
		int z;
		if(!ConfigUtil.doOresAether) return;
		for(int i = 1; i < ConfigUtil.oreChanceAether; i++) { // Number of tries
			Ore ore = new ProbabilityCollection<Ore>()
					.add(new Ore(Material.COAL_ORE, 85), ConfigUtil.getOreWeight("coal_ore"))
					.add(new Ore(Material.IRON_ORE, 50), ConfigUtil.getOreWeight("iron_ore"))
					.add(new Ore(Material.GOLD_ORE, 50), ConfigUtil.getOreWeight("gold_ore"))
					.add(new Ore(Material.REDSTONE_ORE, 65), ConfigUtil.getOreWeight("redstone_ore"))
					.add(new Ore(Material.LAPIS_ORE, 75), ConfigUtil.getOreWeight("lapis_ore"))
					.add(new Ore(Material.DIAMOND_ORE, 50), ConfigUtil.getOreWeight("diamond_ore"))
					.add(new Ore(Material.EMERALD_ORE, 40), ConfigUtil.getOreWeight("emerald_ore")).get();
			x = random.nextInt(15);
			z = random.nextInt(15);
			for(y = 63; chunk.getBlock(x, y, z).getType() != Material.AIR && y > 0; y--);
			if(y > 1) {
				y = random.nextInt(64 - y) + y;
				doVein(world, chunk, random, new int[]{x, y, z}, ore.getType(), ore.getContChance());
			}
		}

	}

	private void doVein(World world, Chunk chunk, Random random, int[] coords, Material ore, int continueChance) {
		int x = coords[0];
		int y = coords[1];
		int z = coords[2];
		if(world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).getType() == Material.STONE) {
			boolean isStone = true;
			while (isStone) {
				world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).setType(ore);
				if(random.nextInt(100) < continueChance) {
					switch(random.nextInt(6)) {
						case 0:
							x++;
							break;
						case 1:
							y++;
							break;
						case 2:
							z++;
							break;
						case 3:
							x--;
							break;
						case 4:
							y--;
							break;
						case 5:
							z--;
							break;
						default:
					}
					isStone = (world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).getType() == Material.STONE)
							&& (world.getBlockAt(x + chunk.getX() * 16, y, z + chunk.getZ() * 16).getType() != ore);
				} else isStone = false;
			}
		}
	}

}