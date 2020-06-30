package com.dfsek.betterend.world.generation.populators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;

import com.dfsek.betterend.Main;
import com.dfsek.betterend.structures.LootTable;
import com.dfsek.betterend.structures.NMSStructure;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.LangUtil;
import com.dfsek.betterend.util.StructureUtil;
import com.dfsek.betterend.world.Biome;

public class CustomStructurePopulator extends BlockPopulator {

	private static Main main = Main.getInstance();
	private static File configFile = new File(main.getDataFolder() + File.separator + "customStructures.yml");
	private static YamlConfiguration config = new YamlConfiguration();
	private static boolean doGeneration = false;
	private static int chancePerChunk;


	static {

		if(Main.isPremium()) {
			main.getLogger().info(LangUtil.ENABLE_STRUCTURES);
			try {
				config.load(configFile);
				doGeneration = true;
			} catch (IOException e) {
				main.getLogger().warning(LangUtil.STRUCTURE_CONFIG_NOT_FOUND);
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}

			chancePerChunk = config.getInt("master-chance-per-chunk", 6);
		}
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		try {
			if(random.nextInt(100) > chancePerChunk || !doGeneration) return;
			if(!(Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20 || ConfigUtil.ALL_AETHER)) return;
			int X = random.nextInt(15);
			int Z = random.nextInt(15);
			if(chunk.getX()*16+X >= 29999900 || chunk.getZ()*16+Z >= 29999900) return;

			NMSStructure structure;

			List<Map<?, ?>> structures = config.getMapList("structures");

			//if(debug) main.getLogger().info("Spawning max of " + maxMobs + ", " + numMobs + " already exist(s).");
			IntStream.Builder structureIDs = IntStream.builder();
			IntStream.Builder weights = IntStream.builder();
			for(int i = 0; i < structures.size(); i++) {
				structureIDs.add(i);
				weights.add((int) structures.get(i).get("weight")); 
			}
			Map<?, ?> struc = structures.get(chooseOnWeight(structureIDs.build().toArray(), weights.build().toArray()));

			if(!((List<?>) struc.get("biomes")).contains(Biome.fromCoordinates(chunk.getX()*16+X, chunk.getZ()*16+Z, world.getSeed()).toString().toUpperCase())) return;

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
				main.getLogger().warning(String.format(LangUtil.INVALID_SPAWN, (String) struc.get("spawn")));
				return;
			}
			Location origin = new Location(world, chunk.getX()*16+X, Y, chunk.getZ()*16+Z);
			
			boolean ground = false;
			
			structure = new NMSStructure(origin, new FileInputStream(main.getDataFolder() + "/structures/" + struc.get("name") + ".nbt"));
			if((boolean) struc.get("override-checks") || StructureUtil.isValidSpawn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1], ground, (boolean) struc.get("strict-check"))) {
				main.getLogger().info(String.format(LangUtil.CUSTOM_STRUCTURE_MSG, struc.get("name"), chunk.getX()*16+X, Y, chunk.getZ()*16+Z));
				structure.paste();
				LootTable table = new LootTable((String) struc.get("name"));
				for(Location location : StructureUtil.getChestsIn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1])) {
					if (location.getBlock().getState() instanceof Container && (boolean) struc.get("populate-loot")) {
						table.populateChest(location, random);
					}
				}
			}
		} catch (NullPointerException e) {
			main.getLogger().warning(LangUtil.STRUCTURE_ERROR);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			main.getLogger().warning(LangUtil.FILE_NOT_FOUND);
		}

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
