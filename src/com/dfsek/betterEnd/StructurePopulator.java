package com.dfsek.betterEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;

public class StructurePopulator extends BlockPopulator {
	Main main = Main.getInstance();
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int X = random.nextInt(15);
		int Z = random.nextInt(15);
		if(chunk.getX()*16+X >= 29999900 || chunk.getZ()*16+Z >= 29999900) return;
		int shulkerSpawns = main.getConfig().getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts");
		if(random.nextInt(100) < main.getConfig().getInt("outer-islands.structures.chance-per-chunk") && Math.sqrt(Math.pow(chunk.getX()*16+X, 2) + Math.pow(chunk.getZ()*16+Z, 2)) >= 1000) {
			File file;
			boolean aboveGround = true;
			int type = random.nextInt(100);
			Location pasteLocation;
			String name;
			boolean spawnShulkers = false;
			boolean overrideSpawnCheck = false;
			if(type < 70) {
				if(Main.getBiomeNoise(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed()) > 0.5) {
					if(random.nextInt(100) < 98) {
						if(random.nextBoolean()) {
							name = "wood_house_s";
							file = new File(main.getDataFolder() + "/scm/wood_house_s/wood_house_s_" + random.nextInt(4) + ".schem");
							int highY;
							for (highY = world.getMaxHeight()-1; chunk.getBlock(X, highY, Z).getType() != Material.GRASS_BLOCK && highY>0; highY--);
							if(highY < 64) return;
							pasteLocation = new Location(world, chunk.getX()*16+X, highY+1, chunk.getZ()*16+Z);
							if(pasteLocation.getBlock().getType() != Material.GRASS_BLOCK && pasteLocation.getBlock().getType() != Material.STONE) {
								pasteLocation = pasteLocation.subtract(0, 1, 0);
							}
						} else {
							name = "cobble_house_s";
							int highY;
							file = new File(main.getDataFolder() + "/scm/cobble_house_s/cobble_house_s_" + random.nextInt(5) + ".schem");
							for (highY = world.getMaxHeight()-1; chunk.getBlock(X, highY, Z).getType() != Material.GRASS_BLOCK && highY>0; highY--);
							if(highY < 64) return;
							pasteLocation = new Location(world, chunk.getX()*16+X, highY, chunk.getZ()*16+Z);
							if(pasteLocation.getBlock().getType() != Material.GRASS_BLOCK && pasteLocation.getBlock().getType() != Material.STONE) {
								pasteLocation = pasteLocation.subtract(0, 1, 0);
							}
						}
					} else {
						if(Main.getBiomeNoise(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed()) < 0.65) return;
						overrideSpawnCheck = true;
						aboveGround = false;
						name = "gold_dungeon";
						file = new File(main.getDataFolder() + "/scm/gold_dungeon/gold_dungeon_0.schem");
						pasteLocation = new Location(world, chunk.getX()*16+X, main.getConfig().getInt("aether.clouds.cloud-height")+8, chunk.getZ()*16+Z);
					}
				} else if(Main.getBiomeNoise(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed()) > -0.5) {
					if(random.nextBoolean()) {
						name = "end_house";
						file = new File(main.getDataFolder() + "/scm/end_house/end_house_" + random.nextInt(3) + ".schem");
						if(world.getHighestBlockYAt(chunk.getX()*16+X, chunk.getZ()*16+Z) < 64) return;
						pasteLocation = new Location(world, chunk.getX()*16+X, world.getHighestBlockYAt(chunk.getX()*16+X, chunk.getZ()*16+Z), chunk.getZ()*16+Z);
					} else {
						name = "shulker_nest";
						spawnShulkers = true;
						file = new File(main.getDataFolder() + "/scm/shulker_nest/shulker_nest_" + random.nextInt(2) + ".schem");
						if(world.getHighestBlockYAt(chunk.getX()*16+X, chunk.getZ()*16+Z) < 64) return;
						pasteLocation = new Location(world, chunk.getX()*16+X, world.getHighestBlockYAt(chunk.getX()*16+X, chunk.getZ()*16+Z), chunk.getZ()*16+Z);
					}
				} else return;
			} else {
				name = "stronghold";
				file = new File(main.getDataFolder() + "/scm/stronghold/stronghold_0.schem");
				pasteLocation = new Location(world, chunk.getX()*16+X, world.getHighestBlockYAt(chunk.getX()*16+X, chunk.getZ()*16+Z)-(random.nextInt(14)+8), chunk.getZ()*16+Z);
				if(pasteLocation.getBlock().getType() != Material.END_STONE) return; 
				aboveGround = false;
			}
			if(pasteLocation.getY() < 1) return;
			BlockVector3 origin = BukkitAdapter.asBlockVector(pasteLocation);

			Clipboard clipboard = null;
			double rotation = random.nextInt(3)*90;
			ClipboardFormat format = ClipboardFormats.findByFile(file);
			try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
				clipboard = reader.read();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 

			BlockVector3 newRotatedMinimumPoint = rotateAround(origin.subtract(clipboard.getOrigin().subtract(clipboard.getRegion().getMinimumPoint())), origin, rotation);
			BlockVector3 newRotatedMaximumPoint = rotateAround(origin.subtract(clipboard.getOrigin().subtract(clipboard.getRegion().getMaximumPoint())), origin, rotation);
			Location minLoc = new Location(pasteLocation.getWorld(), newRotatedMinimumPoint.getX(), newRotatedMinimumPoint.getY(), newRotatedMinimumPoint.getZ());
			Location maxLoc = new Location(pasteLocation.getWorld(), newRotatedMaximumPoint.getX(), newRotatedMaximumPoint.getY(), newRotatedMaximumPoint.getZ());

			//System.out.println(main.getDataFolder());
			if(isValidSpawn(minLoc, maxLoc, aboveGround) || overrideSpawnCheck) {
				System.out.println("[BetterEnd] Generating structure \"" + name + "\" at " + pasteLocation.getBlockX() + ", " + pasteLocation.getBlockY() + ", " + pasteLocation.getBlockZ());
				try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
					AffineTransform transform = new AffineTransform();
					transform = transform.rotateY(rotation);
					ClipboardHolder holder = new ClipboardHolder(clipboard);
					holder.setTransform(transform);
					Operation operation = holder
							.createPaste(editSession)
							.to(BukkitAdapter.asBlockVector(pasteLocation))
							.ignoreAirBlocks(aboveGround)
							.build();
					Operations.complete(operation);

				} catch (WorldEditException e) {
					e.printStackTrace();
				}
			}
			NamespacedKey key = new NamespacedKey(main, "valkyrie-spawner");
			if(spawnShulkers) {
				List<Location> locations = getLocationListBetween(minLoc, maxLoc);
				for(int i = 0; i < shulkerSpawns; i++) {
					boolean done = false;
					int attempts = 0;
					while(!done) {
						Location candidate = locations.get(random.nextInt(locations.size()));
						if(candidate.getBlock().isEmpty()) {
							world.spawn(candidate, Shulker.class);
							done = true;
						}
						attempts++;
						if(attempts > 10) done = true;
					}
				}
			}
			List<Location> locations = getChestsIn(minLoc, maxLoc);
			
			for (Location location : locations) {
				
				if (location.getBlock().getState() instanceof Container) {
					if(name.equals("gold_dungeon")) {
						Chest chest = (Chest) location.getBlock().getState();
						chest.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (int) (rotation/90));
						chest.update();
					}
					JSONArray poolArray = (JSONArray) ((JSONObject) getLootTable(name)).get("pools");
					for (Object pool : poolArray) {
						JSONObject pooldata = (JSONObject) pool;
						int max = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("max"));
						int min = Math.toIntExact((long) ((JSONObject)pooldata.get("rolls")).get("min"));

						JSONArray itemArray = (JSONArray) pooldata.get("entries");
						int rolls = random.nextInt(max-min+1)+min;
						if(main.config.getBoolean("debug")) System.out.println("[BetterEnd] min: " + min + ", max: " + max + ", " + rolls + " rolls.");

						for(int i = 0; i < rolls; i++) {
							int count = 1;
							JSONObject itemdata = (JSONObject) chooseOnWeight(itemArray);
							String itemname = (String) itemdata.get("name");
							double itemDurability = 100;
							if(itemdata.containsKey("functions")) {
								try {
									for (Object function : (JSONArray) itemdata.get("functions")) {
										if(((String) ((JSONObject) function).get("function")).equalsIgnoreCase("set_count")) {
											long maxc = (long) ((JSONObject)((JSONObject)function).get("count")).get("max");
											long minc = (long) ((JSONObject)((JSONObject)function).get("count")).get("min");
											count = random.nextInt(Math.toIntExact(maxc)-Math.toIntExact(minc)) + Math.toIntExact(minc);
										}
										if(((String) ((JSONObject) function).get("function")).equalsIgnoreCase("set_damage")) {
											long maxd = (long) ((JSONObject)((JSONObject)function).get("damage")).get("max");
											long mind = (long) ((JSONObject)((JSONObject)function).get("damage")).get("min");
											itemDurability = (random.nextDouble()*(maxd-mind))+mind;
										}
									}
								} catch(ClassCastException e) {
									if(main.config.getBoolean("debug")) System.out.println("[BetterEnd] Error on item \""+ itemname + "\"");
								}
							}
							if(main.config.getBoolean("debug")) System.out.println("[BetterEnd] "+ itemname + " x" + count + ", durability=" + itemDurability);
							try {
								ItemStack randomItem = new ItemStack(Material.valueOf(itemname.toUpperCase()), count);
								Damageable damage = (Damageable) randomItem.getItemMeta();
								damage.setDamage((int) (Material.valueOf(itemname.toUpperCase()).getMaxDurability()-(itemDurability/100)*Material.valueOf(itemname.toUpperCase()).getMaxDurability()));
								randomItem.setItemMeta((ItemMeta) damage);
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
										if (randomPosItem != null) {
											if (this.isSameItem(randomPosItem, randomItem)) {
												if (randomPosItem.getAmount() < randomItem.getMaxStackSize()) {
													ItemStack randomItemCopy = randomItem.clone();
													int newAmount = randomPosItem.getAmount() + 1;
													randomItemCopy.setAmount(newAmount);
													containerContent[randomPos] = randomItemCopy;
													containerInventory.setContents(containerContent);
													done = true;
												}
											}
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
							} catch(IllegalArgumentException e) {
								System.out.println("[BetterEnd] Invalid item \""+ itemname + "\"");
							}

						}

					}
				}
			}


		} else if(random.nextInt(100) < main.getConfig().getInt("outer-islands.ruins.chance-per-chunk") && Math.sqrt(Math.pow(chunk.getX()*16+X, 2) + Math.pow(chunk.getZ()*16+Z, 2)) >= 1000) {
			File file = new File(main.getDataFolder() + "/scm/stone_ruin/stone_ruin_" + random.nextInt(18) + ".schem");
			int highY;
			for (highY = world.getMaxHeight()-1; chunk.getBlock(X, highY, Z).getType() != Material.GRASS_BLOCK && highY>0; highY--);
			if(highY < 64) return;
			Location pasteLocation = new Location(world, chunk.getX()*16+X, highY+1, chunk.getZ()*16+Z);

			if(pasteLocation.getY() < 1) return;
			BlockVector3 origin = BukkitAdapter.asBlockVector(pasteLocation);

			Clipboard clipboard = null;
			double rotation = random.nextInt(3)*90;
			ClipboardFormat format = ClipboardFormats.findByFile(file);
			try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
				clipboard = reader.read();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 

			BlockVector3 newRotatedMinimumPoint = rotateAround(origin.subtract(clipboard.getOrigin().subtract(clipboard.getRegion().getMinimumPoint())), origin, rotation);
			BlockVector3 newRotatedMaximumPoint = rotateAround(origin.subtract(clipboard.getOrigin().subtract(clipboard.getRegion().getMaximumPoint())), origin, rotation);
			Location minLoc = new Location(pasteLocation.getWorld(), newRotatedMinimumPoint.getX(), newRotatedMinimumPoint.getY(), newRotatedMinimumPoint.getZ());
			Location maxLoc = new Location(pasteLocation.getWorld(), newRotatedMaximumPoint.getX(), newRotatedMaximumPoint.getY(), newRotatedMaximumPoint.getZ());

			//System.out.println(main.getDataFolder());
			if(isValidRuinSpawn(minLoc, maxLoc)) {
				try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
					AffineTransform transform = new AffineTransform();
					transform = transform.rotateY(rotation);
					ClipboardHolder holder = new ClipboardHolder(clipboard);
					holder.setTransform(transform);
					Operation operation = holder
							.createPaste(editSession)
							.to(BukkitAdapter.asBlockVector(pasteLocation))
							.ignoreAirBlocks(true)
							.build();
					Operations.complete(operation);

				} catch (WorldEditException e) {
					e.printStackTrace();
				}
			}
		}

	}
	private boolean isSameItem(ItemStack randomPosItem, ItemStack randomItem) {
		ItemMeta randomPosItemMeta = randomPosItem.getItemMeta();
		ItemMeta randomItemMeta = randomItem.getItemMeta();

		return randomPosItem.getType().equals(randomItem.getType()) && randomPosItemMeta.equals(randomItemMeta);
	}
	public Object chooseOnWeight(JSONArray items) {
		double completeWeight = 0.0;
		for (Object item : items)
			completeWeight += Math.toIntExact((long) ((JSONObject) item).get("weight"));
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (Object item : items) {
			countWeight += Math.toIntExact((long) ((JSONObject) item).get("weight"));
			if (countWeight >= r)
				return item;
		}
		return null;
	}
	private Object getLootTable(String name) {
		InputStream  inputStream = main.getResource("loot/" + name + ".json");

		InputStreamReader isReader = new InputStreamReader(inputStream);

		BufferedReader reader = new BufferedReader(isReader);
		StringBuffer sb = new StringBuffer();
		String str;
		try {
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONParser jsonParser = new JSONParser();
		try {
			return jsonParser.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	private BlockVector3 rotateAround(BlockVector3 point, BlockVector3 center, double angle){
		angle = Math.toRadians(angle * -1);
		double rotatedX = Math.cos(angle) * (point.getX() - center.getX()) - Math.sin(angle) * (point.getZ() - center.getZ()) + center.getX();
		double rotatedZ = Math.sin(angle) * (point.getX() - center.getX()) + Math.cos(angle) * (point.getZ() - center.getZ()) + center.getZ();
		return BlockVector3.at(rotatedX, point.getY(), rotatedZ);
	}
	private boolean isNotAlreadyIn(List<Location> locations, Location location) {
		for (Location auxLocation : locations) {
			if (location.distance(auxLocation) < 1) {
				return false;
			}
		}
		return true;
	}
	private boolean isValidSpawn(Location l1, Location l2, boolean ground) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(l1.getWorld().getSeed(), 4);
		int outNoise = main.getConfig().getInt("outer-islands.noise");
		int lowX = Math.min(l1.getBlockX(), l2.getBlockX());
		int lowY = Math.min(l1.getBlockY(), l2.getBlockY());
		int lowZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
		if(!ground) {
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
				Location loc = new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()) + x, Math.min(l1.getBlockY(), l2.getBlockY())-1, Math.min(l1.getBlockZ(), l2.getBlockZ()) + z);
				if(loc.getBlock().getType() != Material.GRASS_BLOCK && loc.getBlock().getType() != Material.END_STONE && loc.getBlock().getType() != Material.DIRT && loc.getBlock().getType() != Material.STONE) return false;
			}
		}
		return true;
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
			} /*else if (blockState instanceof Sign) {
				locations.add(location);
			}*/
		}
		return locations;
	}
	public static List<Location> getLocationListBetween(Location loc1, Location loc2){
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
}
