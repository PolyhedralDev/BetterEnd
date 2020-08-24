package com.dfsek.betterend.world.population;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.world.generation.biomes.Biome;
import com.dfsek.betterend.world.population.tree.CustomTreeType;
import com.dfsek.betterend.world.population.tree.ShatteredTree;
import com.dfsek.betterend.world.population.tree.WoodTree;
import org.bukkit.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class EnvironmentPopulator extends BlockPopulator {
	private final BetterEnd main = BetterEnd.getInstance();
	@SuppressWarnings("deprecation")
	public void populate(World world, Random random, Chunk chunk) {
		// taiga
		populateTrees(random, chunk, world);
		// trees
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				double biomeNoiseLvl = biomeGenerator.noise((double) (chunk.getX() * 16 + x) / ConfigUtil.biomeSize,
						(double) (chunk.getZ() * 16 + z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
				double heatNoiseLvl = biomeGenerator.noise((double) (chunk.getX() * 16 + x) / ConfigUtil.heatNoise,
						(double) (chunk.getZ() * 16 + z) / ConfigUtil.heatNoise, 0.5D, 0.5D);
				int y;
				for(y = world.getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.SPRUCE_LEAVES) && y > 0; y--);
				if(heatNoiseLvl < -0.5 && random.nextInt(100) < -50 * (heatNoiseLvl + 0.5) && chunk.getBlock(x, y, z).getType() == Material.SPRUCE_LEAVES) {
					chunk.getBlock(x, y + 1, z).setType(Material.SNOW);
				}
				if(heatNoiseLvl < -0.5 && (biomeNoiseLvl > 0.5 || ConfigUtil.allAether)) {
					world.setBiome(chunk.getX() * 16 + x, chunk.getZ() * 16 + z, org.bukkit.block.Biome.TAIGA);

					if(random.nextInt(1000) < 2) {
						for(y = world.getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK && chunk.getBlock(x, y, z).getType() != Material.GRAVEL
								&& chunk.getBlock(x, y, z).getType() != Material.PODZOL && chunk.getBlock(x, y, z).getType() != Material.COARSE_DIRT) && y > 0; y--);
						if(y > 1) world.getBlockAt(chunk.getX() * 16 + x, y + 1, chunk.getZ() * 16 + z)
								.setType((random.nextBoolean()) ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);
					}
				}
			}
		}

		// animals
		if(random.nextInt(100) < ConfigUtil.herdChance) {
			int size = random.nextInt(ConfigUtil.maxHerdSize - ConfigUtil.minHerdSize) + ConfigUtil.minHerdSize;
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
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			if(Biome.fromCoordinates(chunk.getX() * 16 + x, chunk.getZ() * 16 + z, world).isAether()) {
				for(int i = 0; i < size; i++) {
					int y;
					for(y = world.getMaxHeight() - 1; chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK && y > 0; y--);
					if(y > 1) world.spawnEntity(
							new Location(world, (double) chunk.getX() * 16 + x + random.nextInt(3), (double) y + 1, (double) chunk.getZ() * 16 + z + random.nextInt(3)),
							type);
				}
			}
		}
	}
	private void plantLargeTree(CustomTreeType type, Location origin, Random random) {
		if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Generating async tree of type " + type.toString());
		long t = System.nanoTime();
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Building async tree!");
			boolean large = true;
			switch(type) {
				case SHATTERED_SMALL:
					large = false;
				case SHATTERED_LARGE:
					ShatteredTree tree = new ShatteredTree(origin, random, large);
					tree.grow();
					if(ConfigUtil.debug) main.getLogger().info("[" + Thread.currentThread().getName() + "] Time saved: " + (System.nanoTime() - t)/1000000 + "ms");
					Bukkit.getScheduler().runTask(main, () -> tree.plant(false));
					break;
				case SPRUCE:
				case OAK:
					WoodTree woodTree = new WoodTree(origin, random, type);
					woodTree.grow();
					if(ConfigUtil.debug) main.getLogger().info("[" + Thread.currentThread().getName() + "] Time saved: " + (System.nanoTime() - t)/1000000 + "ms");
					Bukkit.getScheduler().runTask(main, () -> woodTree.plant(false));
					break;
				default:
					throw new IllegalArgumentException("Invalid tree type.");
			}

		});
	}
	private void populateTrees(Random random, Chunk chunk, World world) {
		int amount = random.nextInt(ConfigUtil.maxTrees - ConfigUtil.minTrees) + ConfigUtil.minTrees; // Amount
																																																	// of
																																																	// trees
		if((Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20) || ConfigUtil.allAether) for(int i = 0; i < amount; i++) {
			int x = random.nextInt(15);
			int z = random.nextInt(15);
			int y;
			for(y = world.getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK && chunk.getBlock(x, y, z).getType() != Material.PODZOL
					&& chunk.getBlock(x, y, z).getType() != Material.COARSE_DIRT && chunk.getBlock(x, y, z).getType() != Material.SNOW
					&& chunk.getBlock(x, y, z).getType() != Material.END_STONE) && y > 0; y--);
			if(y > ConfigUtil.islandHeight - 1 && y < 255) {
				Location blockLocation = chunk.getBlock(x, y, z).getLocation();
				switch(Biome.fromLocation(blockLocation)) {
					case AETHER:
						for(y = world.getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK) && y > 0; y--);
						if(y > 1) {
							if(random.nextInt(100) < 90) {
								if(random.nextInt(100) < 85) {
									world.generateTree(blockLocation, TreeType.TREE);
								} else {
									world.generateTree(blockLocation, TreeType.BIG_TREE);
								}
							} else {
								if(random.nextInt(100) < 90) {
									world.generateTree(blockLocation, TreeType.BIRCH); 
								} else {
									world.generateTree(blockLocation, TreeType.TALL_BIRCH);
								}
							}
						}
						break;
					case AETHER_FOREST:
						if(i % 2 == 0  && blockLocation.getBlock().getType() == Material.GRASS_BLOCK) {
							plantLargeTree(CustomTreeType.OAK, blockLocation, random);
							return;
						}
						break;
					case AETHER_HIGHLANDS:
						for(int j = 0; j < 16; j++) {
							int x1 = random.nextInt(15);
							int z1 = random.nextInt(15);
							int y1;
							for(y1 = world.getMaxHeight()
									- 1; (chunk.getBlock(x1, y1, z1).getType() != Material.GRASS_BLOCK && chunk.getBlock(x1, y1, z1).getType() != Material.PODZOL
											&& chunk.getBlock(x1, y1, z1).getType() != Material.COARSE_DIRT && chunk.getBlock(x1, y1, z1).getType() != Material.SNOW)
											&& y1 > 0; y1--);
							if(y1 > 1) {
								blockLocation = chunk.getBlock(x, y1, z).getLocation();
								if(blockLocation.getBlock().getType() == Material.SNOW) {
									blockLocation.getBlock().setType(Material.AIR);
									blockLocation.subtract(0, 1, 0);
								}
								if(random.nextInt(100) < 85) {
									world.generateTree(blockLocation, TreeType.REDWOOD);
								} else {
									world.generateTree(blockLocation, TreeType.TALL_REDWOOD);
								}

							}
						}
						break;
					case AETHER_HIGHLANDS_FOREST:
						if(i % 2 == 0 && (blockLocation.getBlock().getType() == Material.GRASS_BLOCK || blockLocation.getBlock().getType() == Material.PODZOL
								|| blockLocation.getBlock().getType() == Material.COARSE_DIRT || blockLocation.getBlock().getType() == Material.SNOW
								|| blockLocation.getBlock().getType() == Material.GRAVEL)) {
							plantLargeTree(CustomTreeType.SPRUCE, blockLocation, random);
							return;
						}
						break;
					case SHATTERED_END:
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 7) {
							if(random.nextInt(100) < 60) {
								plantShatteredPillar(random, chunk, world, new int[]{x, y, z});
							} else {
								int upBound = (int) (random.nextInt((int) ((ConfigUtil.maxObsidianPillarHeight * 0.75) - (ConfigUtil.minObsidianPillarHeight * 0.75)))
										+ (ConfigUtil.minObsidianPillarHeight * 0.75));
								int lowBound = (int) (random.nextInt((int) ((ConfigUtil.maxObsidianPillarHeight * 0.75) - (ConfigUtil.minObsidianPillarHeight * 0.75)))
										+ (ConfigUtil.minObsidianPillarHeight * 0.75));
								for(int j = -lowBound; j < upBound; j++) {
									world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).setType(Material.OBSIDIAN);
								}
							}
						}
						break;
					case SHATTERED_FOREST:
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(20) < 6 && i == 0) {
							plantLargeTree(CustomTreeType.SHATTERED_LARGE, blockLocation, random);
						} else if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(20) < 10) {
							plantLargeTree(CustomTreeType.SHATTERED_SMALL, blockLocation, random);
						}
						break;
					default:
						if(blockLocation.getBlock().getType() == Material.END_STONE && random.nextInt(10) < 8) {
							world.generateTree(blockLocation.add(0, 1, 0), TreeType.CHORUS_PLANT);
						}
						break;
				}
			}
		}

	}

	private void plantShatteredPillar(Random random, Chunk chunk, World world, int[] coords) {
		int x = coords[0];
		int y = coords[1];
		int z = coords[2];
		int[] upBound = {
				random.nextInt((int) ((ConfigUtil.maxObsidianPillarHeight * 0.75) - (ConfigUtil.minObsidianPillarHeight * 0.75) + ConfigUtil.minObsidianPillarHeight)),
				0, 0, 0};
		int[] lowBound = {
				random.nextInt((int) ((ConfigUtil.maxObsidianPillarHeight * 0.75) - (ConfigUtil.minObsidianPillarHeight * 0.75) + ConfigUtil.minObsidianPillarHeight)),
				0, 0, 0};
		int maxH = 0;
		int maxHVal = upBound[0];
		for(int j = 1; j < upBound.length; j++) {
			int addAmount = random.nextInt(7) - 3;
			if(addAmount == 0) addAmount = random.nextBoolean() ? -1 : 1;
			upBound[j] = upBound[j - 1] + addAmount;
			if(upBound[j] > maxHVal) {
				maxH = j;
				maxHVal = upBound[j];
			}
		}
		for(int j = 1; j < lowBound.length; j++) {
			lowBound[j] = lowBound[j - 1] + random.nextInt(7) - 3;
		}
		for(int j = -lowBound[0]; j < upBound[0]; j++) {
			if(world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_ROD
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE_BRICK_SLAB
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE_BRICK_WALL
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z)
							.isPassable()) world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z).setType(Material.OBSIDIAN);
		}
		for(int j = -lowBound[1]; j < upBound[1]; j++) {
			if(world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_ROD
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE_BRICK_SLAB
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z).getType() == Material.END_STONE_BRICK_WALL
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z)
							.isPassable()) world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z).setType(Material.OBSIDIAN);
		}
		for(int j = -lowBound[2]; j < upBound[2]; j++) {
			if(world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_ROD
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE_BRICK_SLAB
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE_BRICK_WALL
					|| world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1)
							.isPassable()) world.getBlockAt((chunk.getX() * 16) + x, y + j, (chunk.getZ() * 16) + z + 1).setType(Material.OBSIDIAN);
		}
		for(int j = -lowBound[3]; j < upBound[3]; j++) {
			if(world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_ROD
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE_BRICK_SLAB
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1).getType() == Material.END_STONE_BRICK_WALL
					|| world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1)
							.isPassable()) world.getBlockAt((chunk.getX() * 16) + x + 1, y + j, (chunk.getZ() * 16) + z + 1).setType(Material.OBSIDIAN);
		}
		if(random.nextInt(100) < 25) {
			switch(maxH) {
				case 0:
					world.spawn(new Location(world, (chunk.getX() * 16) + x + 0.5, (double) upBound[0] + y, (chunk.getZ() * 16) + z + 0.5), EnderCrystal.class,
							enderCrystal -> enderCrystal.setShowingBottom(false));
					break;
				case 1:
					world.spawn(new Location(world, (chunk.getX() * 16) + x + 1.5, (double) upBound[1] + y, (chunk.getZ() * 16) + z + 0.5), EnderCrystal.class,
							enderCrystal -> enderCrystal.setShowingBottom(false));
					break;
				case 2:
					world.spawn(new Location(world, (chunk.getX() * 16) + x + 0.5, (double) upBound[2] + y, (chunk.getZ() * 16) + z + 1.5), EnderCrystal.class,
							enderCrystal -> enderCrystal.setShowingBottom(false));
					break;
				case 3:
					world.spawn(new Location(world, (chunk.getX() * 16) + x + 1.5, (double) upBound[3] + y, (chunk.getZ() * 16) + z + 1.5), EnderCrystal.class,
							enderCrystal -> enderCrystal.setShowingBottom(false));
					break;
				default:
			}
		}
	}

}
