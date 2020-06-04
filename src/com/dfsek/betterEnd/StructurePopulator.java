package com.dfsek.betterEnd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.InventoryHolder;

import com.dfsek.betterEnd.structures.LootTable;
import com.dfsek.betterEnd.structures.NMSStructure;


public class StructurePopulator extends BlockPopulator {
	static Main main = Main.getInstance();
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if(chunk.getX() == 0 && chunk.getZ() == 0) {
			try {
				NMSStructure structure = new NMSStructure("gold_dungeon", 0);
				int[] dimension = structure.getDimensions();
				//DefinedStructure structure = NMSStructure.loadStructure("gold_dungeon", 0); //Load structure from packaged file
				//int[] dimension = NMSStructure.getStructureDimensions(structure);
		        System.out.println("[BetterEnd] X: "+  dimension[0] + ", Y: " + dimension[1] + ", Z: " + dimension[2]);
		        //NMSStructure.pasteStructure(structure, new Location(world, 15, 100, 15), EnumBlockRotation.NONE);
		        Location pasteLocation = new Location(world, 0, 58, 0);
		        
		        structure.paste(pasteLocation, 0);
		        
		        Location[] b = structure.getBoundingLocations(pasteLocation, 0);
		        List<Location> chests = getChestsIn(b[0], b[1]);
		        
		        LootTable table = new LootTable("gold_dungeon");
		        for(Location location : chests) {
		        	if (location.getBlock().getState() instanceof Container) {
		        		table.populateChest(location, random);
		        	}
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	
}
