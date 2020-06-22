package com.dfsek.betterend.populators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.ConfigUtil;
import com.dfsek.betterend.Main;
import com.dfsek.betterend.structures.LootTable;
import com.dfsek.betterend.structures.NMSStructure;

public class CustomStructurePopulator extends BlockPopulator {

	private static Main main = Main.getInstance();
	private static File configFile = new File(main.getDataFolder() + File.separator + "customStructures.yml");
	private static YamlConfiguration config = new YamlConfiguration();
	private static boolean doGeneration = false;
	private static int chancePerChunk;


	static {

		if(Main.isPremium()) {
			main.getLogger().info("Initializing Custom Structure Populator...");
			try {
				config.load(configFile);
				doGeneration = true;
			} catch (IOException e) {
				main.getLogger().warning("Unable to locate customStructures.yml. Aborting custom structure initialization.");
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}

			chancePerChunk = config.getInt("master-chance-per-chunk", 6);
		}
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		try {
			if(random.nextInt(100) < chancePerChunk || !doGeneration) return;
			if(!(Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20 || ConfigUtil.ALL_AETHER)) return;
			int X = random.nextInt(15);
			int Z = random.nextInt(15);
			if(chunk.getX()*16+X >= 29999900 || chunk.getZ()*16+Z >= 29999900) return;

			NMSStructure structure;

			String biome = Main.getBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed());


			List<Map<?, ?>> structures = config.getMapList("structures");

			//if(debug) main.getLogger().info("Spawning max of " + maxMobs + ", " + numMobs + " already exist(s).");
			IntStream.Builder structureIDs = IntStream.builder();
			IntStream.Builder weights = IntStream.builder();
			for(int i = 0; i < structures.size(); i++) {
				structureIDs.add(i);
				weights.add((int) structures.get(i).get("weight")); 
			}
			Map<?, ?> struc = structures.get(chooseOnWeight(structureIDs.build().toArray(), weights.build().toArray()));

			if(!((List<?>) struc.get("biomes")).contains(biome)) return;

			int Y = 0;

			switch((String) struc.get("generate")) {
			case "GROUND":
				for (Y = world.getMaxHeight()-1; new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.GRASS_BLOCK && 
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.END_STONE && 
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.DIRT && 
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.STONE &&
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.PODZOL &&
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.COARSE_DIRT &&
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.GRAVEL &&
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.STONE &&
				new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z).getBlock().getType() != Material.STONE_SLAB && Y>0; Y--);
				Y = Y + (int) struc.get("y-offset");
				break;
			case "AIR":
				Y = random.nextInt((int) struc.get("max-height") - (int) struc.get("min-height")) + (int) struc.get("min-height");
				break;
			default:
				main.getLogger().warning((String) struc.get("spawn") + " is an invalid spawn location. Must be either GROUND or AIR.");
				return;
			}
			Location origin = new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z);
			
			boolean ground = false;
			
			structure = new NMSStructure(origin, new FileInputStream(main.getDataFolder() + "/structures/" + struc.get("name") + ".nbt"));
			if((boolean) struc.get("override-checks") || isValidSpawn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1], ground, (boolean) struc.get("strict-check"))) {
				main.getLogger().info("Generating custom structure " + struc.get("name") + " at " + chunk.getX()*16+X + " " + Y + " " + chunk.getZ()*16+Z);
				structure.paste();
				LootTable table = new LootTable((String) struc.get("name"));
				for(Location location : getChestsIn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1])) {
					if (location.getBlock().getState() instanceof Container && (boolean) struc.get("populate-loot")) {
						table.populateChest(location, random);
					}
				}
			}
		} catch (NullPointerException e) {
			main.getLogger().warning("An error occurred whilst attempting to generate custom structure");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			main.getLogger().warning("The requested custom structure file could not be found.");
		}

	}


	private List<Location> getChestsIn(Location minLoc, Location maxLoc){
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
						if (leftSideLocation.distance(roundedLocation) < 1 && this.isNotAlreadyIn(locations, rightSideLocation)) {
							locations.add(roundedLocation);

						} else if (rightSideLocation.distance(roundedLocation) < 1 && this.isNotAlreadyIn(locations, leftSideLocation)) {
							locations.add(roundedLocation);
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

	private boolean isValidSpawn(Location l1, Location l2, boolean underground, boolean strict) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(l1.getWorld().getSeed(), 4);
		int outNoise = main.getConfig().getInt("outer-islands.noise");
		int lowX = Math.min(l1.getBlockX(), l2.getBlockX());
		int lowY = Math.min(l1.getBlockY(), l2.getBlockY());
		int lowZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
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
				Location l3 = new Location(l1.getWorld(), l1.getX(), Math.min(l1.getBlockY(), l2.getBlockY()), l1.getZ());
				Location l4 = new Location(l1.getWorld(), l1.getX(), Math.min(l1.getBlockY(), l2.getBlockY()), l2.getZ());
				Location l5 = new Location(l1.getWorld(), l2.getX(), Math.min(l1.getBlockY(), l2.getBlockY()), l1.getZ());
				Location l6 = new Location(l1.getWorld(), l2.getX(), Math.min(l1.getBlockY(), l2.getBlockY()), l2.getZ());


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
			} 
		}
		return true;
	}

	public static int chooseOnWeight(int[] items, int[] weights) {
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
		return -1;
	}




}
