package com.dfsek.betterend.populators;

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
import org.bukkit.entity.Shulker;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.ConfigUtil;
import com.dfsek.betterend.Main;
import com.dfsek.betterend.structures.LootTable;
import com.dfsek.betterend.structures.NMSStructure;


public class StructurePopulator extends BlockPopulator {
	private static Main main = Main.getInstance();

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		//if(chunk.getX() == 0 && chunk.getZ() == 0) new Fortress(4, random).build(new Location(world, 0, 96, 0));
		if(!(Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20 || ConfigUtil.ALL_AETHER)) return;
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
		String biome = Main.getBiome(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed());
		if(Y < ConfigUtil.ISLAND_HEIGHT-1 && !"STARFIELD".equals(biome)) return;
		int permutation = 0;
		boolean ground = false;
		boolean overrideSpawnCheck = false;

		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(world.getSeed(), 4);
		double biomeNoiseLvl = biomeGenerator.noise((double) (chunk.getX()*16+X)/ConfigUtil.BIOME_SIZE, (double) (chunk.getZ()*16+Z)/ConfigUtil.BIOME_SIZE, 0.5D, 0.5D);

		if(biomeNoiseLvl > 0.5 || ConfigUtil.ALL_AETHER) {
			if(random.nextInt(100) < ConfigUtil.STRUCTURE_CHANCE) {
				String structureName = chooseOnWeight(new String[] {"gold_dungeon", "cobble_house", "wood_house"}, ConfigUtil.AETHER_STRUCTURE_WEIGHTS);

				switch(structureName) {
				case "cobble_house":
					permutation = random.nextInt(5);
					break;
				case "wood_house":
					Y--;
					if("AETHER_HIGHLANDS".equals(biome) || "AETHER_HIGHLANDS_FOREST".equals(biome)) structureName = "spruce_house";
					permutation = random.nextInt(5);
					break;
				case "gold_dungeon":
					overrideSpawnCheck = true;
					Y = ConfigUtil.CLOUD_HEIGHT;
					break;
				default:
				}
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, permutation);
			} else if(random.nextInt(100) < ConfigUtil.RUIN_CHANCE) {
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), "aether_ruin", random.nextInt(18));
			} else return;
		} else if("STARFIELD".equals(biome)) {
			genStars(random, chunk, world);
			return;
		} else if(!("SHATTERED_END".equals(biome) || "SHATTERED_FOREST".equals(biome))) {
			if(random.nextInt(100) < ConfigUtil.STRUCTURE_CHANCE) {
				String structureName = chooseOnWeight(new String[] {"end_house", "shulker_nest", "stronghold", "end_ship", "end_tower", "wrecked_end_ship"}, ConfigUtil.END_STRUCTURE_WEIGHTS);

				switch(structureName) {
				case "end_house":
					permutation = random.nextInt(3);
					Y = Y - 8;
					break;
				case "wrecked_end_ship":
					permutation = random.nextInt(3);
					Y = Y - 4;
					break;
				case "shulker_nest":
					permutation = random.nextInt(2);
					break;
				case "end_tower":
					permutation = random.nextInt(2);
					break;
				case "stronghold":
					ground = true;
					Y = Y - (random.nextInt(16)+8);
					break;
				case "end_ship":
					overrideSpawnCheck = true;
					permutation = random.nextInt(8);
					Y = ConfigUtil.CLOUD_HEIGHT + (random.nextInt(32)-16);
					break;
				default:
				}
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z), structureName, permutation);
			} else return;
		} else {
			if(random.nextInt(100) < ConfigUtil.STRUCTURE_CHANCE) {
				structure = new NMSStructure(new Location(world, chunk.getX()*16+X, Y - (random.nextInt(16)+8), chunk.getZ()*16+Z), "stronghold", 0);
			} else return;
		}
		structure.setRotation(random.nextInt(4)*90);
		int[] dimension = structure.getDimensions();
		Location[] b = structure.getBoundingLocations();
		if(overrideSpawnCheck || (structure.getName().equals("aether_ruin") ? isValidSpawn(b[0], b[1], false, true) : isValidSpawn(b[0], b[1], ground, false))) {
			structure.paste();
			List<Location> locationsAll = getLocationListBetween(b[0], b[1]);
			if("AETHER_HIGHLANDS".equals(biome) || "AETHER_HIGHLANDS_FOREST".equals(biome)) randomCobwebs(locationsAll, random);
			if("shulker_nest".equals(structure.getName())) spawnShulkers(locationsAll, random, world);
			if(!"aether_ruin".equals(structure.getName())) System.out.println("[BetterEnd] Generating structure \"" + structure.getName() + "\",  at " + b[0].getX() + ", " + b[0].getY() + ", " + b[0].getZ() + ". Dimensions: X: "+  dimension[0] + ", Y: " + dimension[1] + ", Z: " + dimension[2]);
			fillInventories(getChestsIn(b[0], b[1]), random, structure);        
		}
	}
	private void genStars(Random random, Chunk chunk, World world) {
		int Y = random.nextInt(world.getMaxHeight()-20)+10;
		NMSStructure s1 = new NMSStructure(new Location(world, chunk.getX()*16+random.nextInt(16), Y, chunk.getZ()*16+random.nextInt(16)), "void_star", random.nextInt(4));
		Y = random.nextInt(world.getMaxHeight()-20)+10;
		if(random.nextInt(100) < 25) {
			NMSStructure s2 = new NMSStructure(new Location(world, chunk.getX()*16+random.nextInt(16), Y, chunk.getZ()*16+random.nextInt(16)), "void_star", random.nextInt(4));
			boolean p2 = true;
			for(Location l : getLocationListBetween(s2.getBoundingLocations()[0], s2.getBoundingLocations()[1])) {
				if(!l.getBlock().isEmpty() || !Main.getBiome(l.getBlockX(), l.getBlockZ(), l.getWorld().getSeed()).equals("STARFIELD")) {
					p2 = false;
				}
			}
			if(p2) s2.paste();
		}
		boolean p1 = true;
		for(Location l : getLocationListBetween(s1.getBoundingLocations()[0], s1.getBoundingLocations()[1])) {
			if(!l.getBlock().isEmpty() || !Main.getBiome(l.getBlockX(), l.getBlockZ(), l.getWorld().getSeed()).equals("STARFIELD")) {
				p1 = false;
			}
		}
		if(p1) s1.paste();
	}
	private void randomCobwebs(List<Location> locationsAll, Random random) {
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
					candidate.getBlock().getType() == Material.GLASS_PANE ||
					candidate.getBlock().getType() == Material.OAK_SLAB ||
					candidate.getBlock().getType() == Material.OAK_STAIRS) candidate.getBlock().setType(Material.COBWEB);
		}
	}
	private void spawnShulkers(List<Location> locationsAll, Random random, World world) {
		for(int i = 0; i < ConfigUtil.SHULKER_SPAWNS; i++) {
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
	private void fillInventories(List<Location> chests, Random random, NMSStructure structure) {
		LootTable table = ("aether_ruin".equals(structure.getName())) ? null : new LootTable(structure.getName());
		for(Location location : chests) {
			if (location.getBlock().getState() instanceof Container) {
				if("end_ship".equals(structure.getName()) && location.getBlock().getState() instanceof Container && (location.getBlock().getType() == Material.DISPENSER)) {
					populateTNT(random, location);
				}
				if("gold_dungeon".equals(structure.getName())) {
					NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
					Chest chest = (Chest) location.getBlock().getState();
					chest.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (int) (structure.getRotation()/90));
					chest.update();
					if(ConfigUtil.ENABLE_MYTHIC_BOSS) table = new LootTable("gold_dungeon_boss");
				}
				table.populateChest(location, random);
			}
		}
	}
	private boolean isSameItem(ItemStack randomPosItem, ItemStack randomItem) {
		ItemMeta randomPosItemMeta = randomPosItem.getItemMeta();
		ItemMeta randomItemMeta = randomItem.getItemMeta();

		return randomPosItem.getType().equals(randomItem.getType()) && randomPosItemMeta.equals(randomItemMeta);
	}
	private void populateTNT(Random random, Location location) {
		int numTNT = random.nextInt(4)+2;
		ItemStack randomItem = new ItemStack(Material.TNT, numTNT);
		BlockState blockState = location.getBlock().getState();
		Container container = (Container) blockState;
		Inventory containerInventory = container.getInventory();
		ItemStack[] containerContent = containerInventory.getContents();
		for (int j = 0; j < randomItem.getAmount(); j++) {
			boolean done = false;
			int attemps = 0;
			while (!done) {
				int randomPos = random.nextInt(containerContent.length);
				ItemStack randomPosItem = containerInventory.getItem(randomPos);
				if (randomPosItem != null && this.isSameItem(randomPosItem, randomItem) && randomPosItem.getAmount() < randomItem.getMaxStackSize()) {
					ItemStack randomItemCopy = randomItem.clone();
					int newAmount = randomPosItem.getAmount() + 1;
					randomItemCopy.setAmount(newAmount);
					containerContent[randomPos] = randomItemCopy;
					containerInventory.setContents(containerContent);
					done = true;
				} else {
					ItemStack randomItemCopy = randomItem.clone();
					randomItemCopy.setAmount(1);
					containerContent[randomPos] = randomItemCopy;
					containerInventory.setContents(containerContent);
					done = true;
				}
				attemps++;
				if (attemps >= containerContent.length) {
					done = true;
				}
			}
		}
	}
	private String chooseOnWeight(String[] items, int[] weights) {
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

}
