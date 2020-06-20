package com.dfsek.betterend.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

import com.dfsek.betterend.Main;

public class MeteorPopulator extends BlockPopulator {
	private Main main = Main.getInstance();
	private int min = main.getConfig().getInt("outer-islands.meteors.min-size", 3); 
	private int max = main.getConfig().getInt("outer-islands.meteors.max-size", 5);
	private int chance = main.getConfig().getInt("outer-islands.meteors.chance-per-chunk", 8);

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		/*if(random.nextInt(100) < chance) {
			int X = random.nextInt(16);
			int Z = random.nextInt(16);
			int Y;
			for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() == Material.WHITE_STAINED_GLASS ||
					chunk.getBlock(X, Y, Z).getType() == Material.AIR) && Y>0; Y--);
			if(Y < 1) return;
			int radius = random.nextInt(max-min+1)+min;
			Location start = new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).add(0,5,0);
			
			int radiusn = (int) (radius * 2.5)+1;
			for (int x = (int) -radiusn; x <= radiusn; x++) {
				for (int y = (int) -radiusn; y <= radiusn; y++) {
					for (int z = (int) -radiusn; z <= radiusn; z++) {
						Vector position = start.toVector().clone().add(new Vector(x, y, z));

						if (start.toVector().distance(position) <= radiusn + 0.5 && y < 0 && position.toLocation(start.getWorld()).getBlock().getType() == Material.AIR) {
							return;
						}
					}
				}
			}
			for (int x = (int) -radiusn; x <= radiusn; x++) {
				for (int y = (int) -radiusn; y <= radiusn; y++) {
					for (int z = (int) -radiusn; z <= radiusn; z++) {
						Vector position = start.toVector().clone().add(new Vector(x, y, z));

						if (start.toVector().distance(position) <= radiusn + 0.5) {
							position.toLocation(start.getWorld()).getBlock().setType(Material.AIR);
						}
					}
				}
			}
			Vector orient = new Vector(random.nextInt(3)+3, 0, 0).rotateAroundY(random.nextInt(360));
			for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() == Material.WHITE_STAINED_GLASS ||
					chunk.getBlock(X, Y, Z).getType() == Material.AIR) && Y>0; Y--);
			if(Y < 1) Y = 64;
			start.add(orient).setY(Y);
			for (int x = (int) -radius; x <= radius; x++) {
				for (int y = (int) -radius; y <= radius; y++) {
					for (int z = (int) -radius; z <= radius; z++) {
						Vector position = start.toVector().clone().add(new Vector(x, y, z));

						if (start.toVector().distance(position) <= radius + 0.5) {
							position.toLocation(start.getWorld()).getBlock().setType(Material.OBSIDIAN);
						}
					}
				}
			}
			
		}*/

	}

}
