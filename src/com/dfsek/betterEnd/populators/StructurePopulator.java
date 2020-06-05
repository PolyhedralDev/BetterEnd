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
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import com.dfsek.betterEnd.Main;
import com.dfsek.betterEnd.structures.LootTable;
import com.dfsek.betterEnd.structures.NMSStructure;


public class StructurePopulator extends BlockPopulator {
	static Main main = Main.getInstance();

	int shulkerSpawns = main.getConfig().getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts");
	boolean allAether = main.getConfig().getBoolean("all-aether", false);
	int structureChance = main.getConfig().getInt("outer-islands.structures.chance-per-chunk");
	int ruinChance = main.getConfig().getInt("outer-islands.ruins.chance-per-chunk");
	int cloudHeight = main.getConfig().getInt("aether.clouds.cloud-height");

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int X = random.nextInt(15);
		int Z = random.nextInt(15);
		if(chunk.getX()*16+X >= 29999900 || chunk.getZ()*16+Z >= 29999900) return;
		NMSStructure structure;
		int Y;
		for (Y = world.getMaxHeight()-1; (chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK &&
				chunk.getBlock(X, Y, Z).getType() != Material.GRAVEL &&
				chunk.getBlock(X, Y, Z).getType() != Material.PODZOL &&
				chunk.getBlock(X, Y, Z).getType() != Material.END_STONE &&
				chunk.getBlock(X, Y, Z).getType() != Material.COARSE_DIRT) && Y>0; Y--);
		if(Y < 1) return;
		int permutations = 1;
		boolean ground = false;
		boolean overrideSpawnCheck = false;
		
		switch(Main.getBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed())) {
		case "AETHER":
			if(random.nextInt(100) < structureChance) {
				String structureName = chooseOnWeight(new String[] {"gold_dungeon", "cobble_house", "wood_house"}, new int[] {2, 49, 49});

				switch(structureName) {
				case "cobble_house":
					Y++;
					permutations = 5;
					break;
				case "wood_house":
					Y--;
					permutations = 4;
					break;
				case "gold_dungeon":
					overrideSpawnCheck = true;
					Y = cloudHeight + 8;
				}


				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, random.nextInt(permutations));
			} else if(random.nextInt(100) < ruinChance) {
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), "aether_ruin", random.nextInt(18));
			} else return;
			break;
		default:
			if(random.nextInt(100) < structureChance) {
				String structureName = chooseOnWeight(new String[] {"end_house", "shulker_nest", "stronghold"}, new int[] {35, 35, 30});
				switch(structureName) {
				case "end_house":
					Y--;
					permutations = 3;
					break;
				case "shulker_nest":
					permutations = 2;
					break;
				case "stronghold":
					ground = true;
					Y = Y - (random.nextInt(16)+8);
				}
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, random.nextInt(permutations));
			} else return;
		}

		int[] dimension = structure.getDimensions();
		Location[] b = structure.getBoundingLocations();
		if(overrideSpawnCheck || (structure.getName().equals("aether_ruin") ? isValidRuinSpawn(b[0], b[1]) : isValidSpawn(b[0], b[1], ground)))
		structure.paste();

		

		b[0].getBlock().setType(Material.BEDROCK);
		b[1].getBlock().setType(Material.BEDROCK);
		System.out.println("[BetterEnd] Generating structure \"" + structure.getName() + "\",  at " + b[0].getX() + ", " + b[0].getY() + ", " + b[0].getZ() + ". Dimensions: X: "+  dimension[0] + ", Y: " + dimension[1] + ", Z: " + dimension[2]);
		List<Location> chests = getChestsIn(b[0], b[1]);        
		LootTable table = (structure.getName().contentEquals("aether_ruin")) ? null : new LootTable(structure.getName());

		for(Location location : chests) {
			if (location.getBlock().getState() instanceof Container) {
				if(structure.getName().equals("gold_dungeon")) {
					NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
					Chest chest = (Chest) location.getBlock().getState();
					chest.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (int) (structure.getRotation()/90));
					chest.update();
					if(main.getConfig().getBoolean("aether.mythic-boss.enable", false)) table = new LootTable("gold_dungeon_boss");
				}
				table.populateChest(location, random);
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
	private boolean isValidSpawn(Location l1, Location l2, boolean underground) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(l1.getWorld().getSeed(), 4);
		int outNoise = main.getConfig().getInt("outer-islands.noise");
		int lowX = Math.min(l1.getBlockX(), l2.getBlockX());
		int lowY = Math.min(l1.getBlockY(), l2.getBlockY());
		int lowZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
		if(underground) {
			if (l1.getBlock().isEmpty()) {
				return false;
			}
			if (l2.getBlock().isEmpty()) {
				return false;
			}
			Location l3 = new Location(l1.getWorld(), l1.getBlockX(), l2.getBlockY(), l1.getBlockZ());
			Location l4 = new Location(l2.getWorld(), l2.getBlockX(), l1.getBlockY(), l2.getBlockZ());
			if (l3.getBlock().isEmpty()) {
				return false;
			}
			if (l4.getBlock().isEmpty()) {
				return false;
			}
		} else {
			List<Location> locs = new ArrayList<>();
			for(int x = 0; x<= Math.abs(l1.getBlockX() - l2.getBlockX()); x++){
				for(int z = 0; z<= Math.abs(l1.getBlockZ() - l2.getBlockZ()); z++){
					locs.add(new Location(l1.getWorld(), lowX + x, lowY, lowZ + z));
				}
			}
			for (Location location : locs) {
				if (generator.noise((double) (location.getBlockX())/outNoise, (double) (location.getBlockZ())/outNoise, 0.1D, 0.55D) < 0.45) {
					return false;
				}
			}
		}
		return true;
	}
	private boolean isValidRuinSpawn(Location l1, Location l2) {
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
		return true;
	}

}
