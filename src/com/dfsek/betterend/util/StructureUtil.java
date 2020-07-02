package com.dfsek.betterend.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.dfsek.betterend.Main;

public class StructureUtil {
	private static Main main = Main.getInstance();
	private StructureUtil() {}
	public static boolean isValidSpawn(Location l1, Location l2, boolean underground, boolean strict) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(l1.getWorld().getSeed(), 4);
		int outNoise = main.getConfig().getInt("outer-islands.noise");
		int lowX = Math.min(l1.getBlockX(), l2.getBlockX());
		int lowY = Math.min(l1.getBlockY(), l2.getBlockY());
		int lowZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
		List<Location> locs = new ArrayList<>();
		for(int x = 0; x<= Math.abs(l1.getBlockX() - l2.getBlockX()); x++){
			for(int z = 0; z<= Math.abs(l1.getBlockZ() - l2.getBlockZ()); z++){
				locs.add(new Location(l1.getWorld(), (double) lowX + x, (double) lowY, (double) lowZ + z));
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
			Location l3 = new Location(l1.getWorld(), l2.getBlockX(), l1.getBlockY(), l1.getBlockZ());
			Location l4 = new Location(l2.getWorld(), l1.getBlockX(), l2.getBlockY(), l1.getBlockZ());
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
						Location loc = new Location(l1.getWorld(), Math.min(l1.getBlockX(), (double) l2.getBlockX()) + x, Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), (double) l2.getBlockZ()) + z);
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
	public static List<Location> getChestsIn(Location minLoc, Location maxLoc){
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
						if ((leftSideLocation.distance(roundedLocation) < 1 && isNotAlreadyIn(locations, rightSideLocation)) || (rightSideLocation.distance(roundedLocation) < 1 && isNotAlreadyIn(locations, leftSideLocation))) {
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
	public static List<Location> getLocationListBetween(Location loc1, Location loc2){
		int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

		List<Location> locs = new ArrayList<>();
		for(int x = 0; x<= Math.abs(loc1.getBlockX() - loc2.getBlockX()); x++){
			for(int y = 0; y<= Math.abs(loc1.getBlockY() - loc2.getBlockY()); y++){
				for(int z = 0; z<= Math.abs(loc1.getBlockZ() - loc2.getBlockZ()); z++){
					locs.add(new Location(loc1.getWorld(), (double) lowX + x, (double) lowY + y, (double) lowZ + z));
				}
			}
		}
		return locs;
	}
	private static boolean isNotAlreadyIn(List<Location> locations, Location location) {
		for (Location auxLocation : locations) {
			if (location.distance(auxLocation) < 1) {
				return false;
			}
		}
		return true;
	}
}
