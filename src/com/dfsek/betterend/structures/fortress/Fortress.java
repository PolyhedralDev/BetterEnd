package com.dfsek.betterend.structures.fortress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import com.dfsek.betterend.structures.NMSStructure;

public class Fortress {
	private List<List<FortressNode>> nodes = new ArrayList<List<FortressNode>>();
	//private List<List<FortressNodeType>> branches = new ArrayList<List<FortressNodeType>>();
	private Random random;
	private int size;
	public Fortress(int size, Random random) {
		this.random = random;
		this.size = size;
	}
	public void build(Location origin) {
		new NMSStructure(origin.clone(), "end_fortress/end_fortress_b_cross_" + random.nextInt(2)).paste();
		int x = 0;
		int z = 0;
		origin.getBlock().setType(Material.DIAMOND_BLOCK);
		origin.clone().add(0,26,0).getBlock().setType(Material.DIAMOND_BLOCK);
		origin.clone().add(21,26,0).getBlock().setType(Material.DIAMOND_BLOCK);
		origin.clone().add(0,26,21).getBlock().setType(Material.DIAMOND_BLOCK);
		origin.clone().add(21,26,21).getBlock().setType(Material.DIAMOND_BLOCK);
		int counter = 0;
		while(!isFortressSatisfied(origin.clone()) && counter < 100000) {

			for(Location l : getLocationListBetween(origin.clone().add(size, 26, size), origin.clone().subtract(size, -26, size))) {
				
				if(l.getBlock().getType() == Material.BEDROCK) {
					l.getBlock().setType(Material.AIR);
				} else l.getBlock().setType(Material.DIAMOND_BLOCK);
			}
			counter++;
		}

	}
	private boolean isFortressSatisfied(Location origin) {
		for(Location l : getLocationListBetween(origin.clone().add(size*21, 26, size*21), origin.clone().subtract(size*21, -26, size*21))) {
			if(l.getBlock().getType() == Material.BEDROCK) return false;
			//if(l.getBlockY() == origin.getBlockY() + 26) l.getBlock().setType(Material.GLASS);
			//else l.getBlock().setType(Material.BLUE_STAINED_GLASS);
		}
		return true;
	}
	private static List<Location> getLocationListBetween(Location loc1, Location loc2){
		int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

		List<Location> locs = new ArrayList<>();
		for(int x = 0; x<= Math.abs(loc1.getBlockX() - loc2.getBlockX()); x++){
			int y = 26;
			for(int z = 0; z<= Math.abs(loc1.getBlockZ() - loc2.getBlockZ()); z++){
				locs.add(new Location(loc1.getWorld(), lowX + x, lowY + y, lowZ + z));
			}
		}
		return locs;
	}
}
