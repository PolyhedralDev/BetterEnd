package com.dfsek.betterEnd.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Shulker;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import com.dfsek.betterEnd.Main;
import com.dfsek.betterEnd.structures.LootTable;
import com.dfsek.betterEnd.structures.NMSStructure;


public class StructurePopulator extends BlockPopulator {
	static Main main = Main.getInstance();
	FileConfiguration config = main.getConfig();
	int shulkerSpawns = config.getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts");
	boolean allAether = config.getBoolean("all-aether", false);
	int structureChance = config.getInt("outer-islands.structures.chance-per-chunk");
	int ruinChance = config.getInt("outer-islands.ruins.chance-per-chunk");
	int cloudHeight = config.getInt("aether.clouds.cloud-height");

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if(!(Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20 || allAether)) return;
		int X = random.nextInt(15);
		int Z = random.nextInt(15);
		if(chunk.getX()*16+X >= 29999900 || chunk.getZ()*16+Z >= 29999900) return;
		NMSStructure structure;
		int Y;
		for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK &&
				chunk.getBlock(X, Y, Z).getType() != Material.GRAVEL &&
				chunk.getBlock(X, Y, Z).getType() != Material.PODZOL &&
				chunk.getBlock(X, Y, Z).getType() != Material.END_STONE &&
				chunk.getBlock(X, Y, Z).getType() != Material.DIRT &&
				chunk.getBlock(X, Y, Z).getType() != Material.STONE &&
				chunk.getBlock(X, Y, Z).getType() != Material.COARSE_DIRT) && Y>0; Y--);
		if(Y < 1) return;
		int permutation = 0;
		boolean ground = false;
		boolean overrideSpawnCheck = false;
		String biome = Main.getBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed());
		if(biome.equals("AETHER") || biome.equals("AETHER_HIGHLANDS")) {
			if(random.nextInt(100) < structureChance) {
				int[] weights = {config.getInt("structure-weight.aether.gold_dungeon"), config.getInt("structure-weight.aether.cobble_house"), config.getInt("structure-weight.aether.wood_house")};
				String structureName = chooseOnWeight(new String[] {"gold_dungeon", "cobble_house", "wood_house"}, weights);

				switch(structureName) {
				case "cobble_house":
					permutation = random.nextInt(5);
					break;
				case "wood_house":
					Y--;
					permutation = random.nextInt(4);
					break;
				case "gold_dungeon":
					overrideSpawnCheck = true;
					Y = cloudHeight + 8;
				}


				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, permutation);
			} else if(random.nextInt(100) < ruinChance) {
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), "aether_ruin", random.nextInt(18));
			} else return;
		} else if(!biome.equals("SHATTERED_END")) {
			if(random.nextInt(100) < structureChance) {
				int[] weights = {config.getInt("structure-weight.end.end_house"), config.getInt("structure-weight.end.shulker_nest"), config.getInt("structure-weight.end.stronghold")};
				String structureName = chooseOnWeight(new String[] {"end_house", "shulker_nest", "stronghold"}, weights);

				switch(structureName) {
				case "end_house":
					permutation = random.nextInt(3);
					Y = Y - 4;
					break;
				case "shulker_nest":
					permutation = random.nextInt(2);
					break;
				case "stronghold":
					ground = true;
					Y = Y - (random.nextInt(16)+8);
				}
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, permutation);
			} else return;
		} else {
			if(random.nextInt(100) < structureChance) {
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y - (random.nextInt(16)+8), chunk.getZ()*16+Z), "stronghold", 0);
			} else return;
		}
		structure.setRotation(random.nextInt(4)*90);
		int[] dimension = structure.getDimensions();
		Location[] b = structure.getBoundingLocations();
		if(overrideSpawnCheck || (structure.getName().equals("aether_ruin") ? isValidSpawn(b[0], b[1], false, 0, true) : isValidSpawn(b[0], b[1], ground, structure.getName().equals("wood_house") ? -2 : 1, false))) {
			structure.paste();
			List<Location> locationsAll = getLocationListBetween(b[0], b[1]);
			if(biome.equals("AETHER_HIGHLANDS")) {

				for(int i = 0; i < (locationsAll.size()/12)+1; i++) {
					Location candidate = locationsAll.get(random.nextInt(locationsAll.size()));
					if(candidate.getBlock().getType() == Material.OAK_LOG ||
							candidate.getBlock().getType() == Material.OAK_PLANKS ||
							candidate.getBlock().getType() == Material.COBBLESTONE ||
							candidate.getBlock().getType() == Material.COBBLESTONE_SLAB ||
							candidate.getBlock().getType() == Material.COBBLESTONE_STAIRS ||
							candidate.getBlock().getType() == Material.CHISELED_STONE_BRICKS ||
							candidate.getBlock().getType() == Material.STONE_BRICKS ||
							candidate.getBlock().getType() == Material.CRACKED_STONE_BRICKS ||
							candidate.getBlock().getType() == Material.MOSSY_STONE_BRICKS ||
							candidate.getBlock().getType() == Material.MOSSY_STONE_BRICK_SLAB ||
							candidate.getBlock().getType() == Material.MOSSY_STONE_BRICK_STAIRS ||
							candidate.getBlock().getType() == Material.STONE_BRICK_SLAB ||
							candidate.getBlock().getType() == Material.STONE_BRICK_STAIRS ||
							candidate.getBlock().getType() == Material.GLASS_PANE) candidate.getBlock().setType(Material.COBWEB);
				}
			}
			if(structure.getName().contentEquals("shulker_nest")) {
				for(int i = 0; i < shulkerSpawns; i++) {
					boolean done = false;
					int attempts = 0;
					while(!done) {
						Location candidate = locationsAll.get(random.nextInt(locationsAll.size()));
						if(candidate.getBlock().isEmpty() && (
								(!candidate.add(1,0,0).getBlock().isEmpty() && !candidate.add(1,0,0).getBlock().getType().toString().contains("slab") && !candidate.add(1,0,0).getBlock().getType().toString().contains("stair")) || 
								(!candidate.add(0,1,0).getBlock().isEmpty() && !candidate.add(0,1,0).getBlock().getType().toString().contains("slab") && !candidate.add(0,1,0).getBlock().getType().toString().contains("stair")) || 
								(!candidate.add(0,0,1).getBlock().isEmpty() && !candidate.add(0,0,1).getBlock().getType().toString().contains("slab") && !candidate.add(0,0,1).getBlock().getType().toString().contains("stair")) ||
								(!candidate.subtract(1,0,0).getBlock().isEmpty() && !candidate.subtract(1,0,0).getBlock().getType().toString().contains("slab") && !candidate.subtract(1,0,0).getBlock().getType().toString().contains("stair")) ||
								(!candidate.subtract(0,1,0).getBlock().isEmpty() && !candidate.subtract(0,1,0).getBlock().getType().toString().contains("slab") && !candidate.subtract(0,1,0).getBlock().getType().toString().contains("stair")) ||
								(!candidate.subtract(0,0,1).getBlock().isEmpty() && !candidate.subtract(0,0,1).getBlock().getType().toString().contains("slab") && !candidate.subtract(0,0,1).getBlock().getType().toString().contains("stair")))) {
							world.spawn(candidate, Shulker.class);
							done = true;
						}
						attempts++;
						if(attempts > 15) done = true;
					}
				}
			}
			//b[0].getBlock().setType(Material.BEDROCK);
			//b[1].getBlock().setType(Material.BEDROCK);
			if(!structure.getName().contentEquals("aether_ruin")) System.out.println("[BetterEnd] Generating structure \"" + structure.getName() + "\",  at " + b[0].getX() + ", " + b[0].getY() + ", " + b[0].getZ() + ". Dimensions: X: "+  dimension[0] + ", Y: " + dimension[1] + ", Z: " + dimension[2]);
			List<Location> chests = getChestsIn(b[0], b[1]);        
			LootTable table = (structure.getName().contentEquals("aether_ruin")) ? null : new LootTable(structure.getName());

			for(Location location : chests) {
				if (location.getBlock().getState() instanceof Container) {
					if(structure.getName().equals("gold_dungeon")) {
						NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
						Chest chest = (Chest) location.getBlock().getState();
						chest.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (int) (structure.getRotation()/90));
						chest.update();
						if(config.getBoolean("aether.mythic-boss.enable", false)) table = new LootTable("gold_dungeon_boss");
					}
					table.populateChest(location, random);
				}
			}
		}
	}
	public String chooseOnWeight(String[] items, int[] weights) {
		double completeWeight = 0.0;
		for (int weight : weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if (countWeight >= r)
				return items[i];
		}
		return null;
	}
	public List<Location> getChestsIn(Location minLoc, Location maxLoc){
		List<Location> locations = new ArrayList<>();
		for (Location location : getLocationListBetween(minLoc, maxLoc)) {
			BlockState blockState = location.getBlock().getState();
			if (blockState instanceof Container) {
				if (blockState instanceof Chest) {
					InventoryHolder holder = ((Chest) blockState).getInventory().getHolder();
					if (holder instanceof DoubleChest) {
						DoubleChest doubleChest = ((DoubleChest) holder);
						Location leftSideLocation = ((Chest) doubleChest.getLeftSide()).getLocation();
						Location rightSideLocation = ((Chest) doubleChest.getRightSide()).getLocation();

						Location roundedLocation = new Location(location.getWorld(),
								Math.floor(location.getX()), Math.floor(location.getY()),
								Math.floor(location.getZ()));

						// Check to see if this (or the other) side of the chest is already in the list
						if (leftSideLocation.distance(roundedLocation) < 1) {
							if (this.isNotAlreadyIn(locations, rightSideLocation)) {
								locations.add(roundedLocation);
							}

						} else if (rightSideLocation.distance(roundedLocation) < 1) {
							if (this.isNotAlreadyIn(locations, leftSideLocation)) {
								locations.add(roundedLocation);
							}
						}

					} else if (holder instanceof Chest) {
						locations.add(location);
					}
				} else {
					locations.add(location);
				}
			}
		}
		return locations;
	}

	private static List<Location> getLocationListBetween(Location loc1, Location loc2){
		int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

		List<Location> locs = new ArrayList<>();
		for(int x = 0; x<= Math.abs(loc1.getBlockX() - loc2.getBlockX()); x++){
			for(int y = 0; y<= Math.abs(loc1.getBlockY() - loc2.getBlockY()); y++){
				for(int z = 0; z<= Math.abs(loc1.getBlockZ() - loc2.getBlockZ()); z++){
					locs.add(new Location(loc1.getWorld(), lowX + x, lowY + y, lowZ + z));
				}
			}
		}
		return locs;
	}
	private boolean isNotAlreadyIn(List<Location> locations, Location location) {
		for (Location auxLocation : locations) {
			if (location.distance(auxLocation) < 1) {
				return false;
			}
		}
		return true;
	}
	private boolean isValidSpawn(Location l1, Location l2, boolean underground, int sub, boolean strict) {
		if(underground) {
			if (l1.getBlock().isEmpty()) {
				return false;
			}
			if (l2.getBlock().isEmpty()) {
				return false;
			}
			//       l1 = new Location(l1.getWorld(), l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
			Location l3 = new Location(l1.getWorld(), l2.getBlockX(), l1.getBlockY(), l1.getBlockZ());
			Location l4 = new Location(l2.getWorld(), l1.getBlockX(), l2.getBlockY(), l1.getBlockZ());
			//	     l2 = new Location(l2.getWorld(), l2.getBlockX(), l2.getBlockY(), l2.getBlockZ());
			Location l5 = new Location(l1.getWorld(), l1.getBlockX(), l1.getBlockY(), l2.getBlockZ());
			Location l6 = new Location(l2.getWorld(), l2.getBlockX(), l1.getBlockY(), l2.getBlockZ());
			Location l7 = new Location(l1.getWorld(), l1.getBlockX(), l2.getBlockY(), l1.getBlockZ());
			Location l8 = new Location(l2.getWorld(), l2.getBlockX(), l2.getBlockY(), l1.getBlockZ());
			if (l3.getBlock().isEmpty()) {
				return false;
			}
			if (l4.getBlock().isEmpty()) {
				return false;
			}
			if (l5.getBlock().isEmpty()) {
				return false;
			}
			if (l6.getBlock().isEmpty()) {
				return false;
			}
			if (l7.getBlock().isEmpty()) {
				return false;
			}
			if (l8.getBlock().isEmpty()) {
				return false;
			}
		} else {
			if(strict) {
				for(int x = 0; x<= Math.abs(l1.getBlockX() - l2.getBlockX()); x++){
					for(int z = 0; z<= Math.abs(l1.getBlockZ() - l2.getBlockZ()); z++){
						Location loc = new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()) + x, Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()) + z);
						if(loc.getBlock().getType() != Material.GRASS_BLOCK && 
								loc.getBlock().getType() != Material.END_STONE && 
								loc.getBlock().getType() != Material.DIRT && 
								loc.getBlock().getType() != Material.STONE &&
								loc.getBlock().getType() != Material.PODZOL &&
								loc.getBlock().getType() != Material.COARSE_DIRT &&
								loc.getBlock().getType() != Material.GRAVEL) return false;
					}
				}
			} else {
				Location l3 = new Location(l1.getWorld(), l1.getX(), l1.getY(), l2.getZ());
				Location l4 = new Location(l1.getWorld(), l2.getX(), l1.getY(), l2.getZ());
				Location l5 = new Location(l1.getWorld(), l2.getX(), l1.getY(), l2.getZ());

				if (l1.getBlock().isEmpty()) {
					return false;
				}
				if (l3.getBlock().isEmpty()) {
					return false;
				}
				if (l4.getBlock().isEmpty()) {
					return false;
				}
				if (l5.getBlock().isEmpty()) {
					return false;
				}
			} 
		}
		return true;
	}

}