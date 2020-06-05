package com.dfsek.betterEnd.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

import com.dfsek.betterEnd.Main;

public class HerdPopulator extends BlockPopulator {
	Main main = Main.getInstance();
	int herdChance = main.getConfig().getInt("aether.animals.herd-chance-per-chunk", 15);
	int min = main.getConfig().getInt("aether.animals.herd-min-size", 2);
	int max = main.getConfig().getInt("aether.animals.herd-max-size", 5);
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if(random.nextInt(100) < herdChance) {
			int size = random.nextInt(max-min)+min;
			EntityType type;
			switch(random.nextInt(3)) {
			case 0:
				type = EntityType.CHICKEN;
				break;
			case 1:
				type = EntityType.COW;
				break;
			default:
				type = EntityType.SHEEP;
				break;
			}
			int X = random.nextInt(16);
			int Z = random.nextInt(16);
			if(Main.getBiome(chunk.getX()*16 + X, chunk.getZ()*16 + Z, world.getSeed()).equals("AETHER") || Main.getBiome(chunk.getX()*16 + X, chunk.getZ()*16 + Z, world.getSeed()).equals("AETHER_HIGHLANDS")) {
				for (int i = 0; i < size; i++) {
					int Y;
					for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && Y>0; Y--);
					if(Y > 1) world.spawnEntity(new Location(world, chunk.getX()*16 + X + random.nextInt(3), Y + 1, chunk.getZ()*16 + Z + random.nextInt(3)), type);
				}
			}
		}

	}

}
