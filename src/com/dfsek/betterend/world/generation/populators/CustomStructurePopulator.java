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

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.structures.LootTable;
import com.dfsek.betterend.structures.NMSStructure;
import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.LangUtil;
import com.dfsek.betterend.util.StructureUtil;
import com.dfsek.betterend.world.Biome;
import org.jetbrains.annotations.NotNull;

public class CustomStructurePopulator extends BlockPopulator {

	private static final BetterEnd main = BetterEnd.getInstance();
	private static final File configFile = new File(main.getDataFolder() + File.separator + "customStructures.yml");
	private static final YamlConfiguration config = new YamlConfiguration();
	private static boolean doGeneration = false;
	private static int chancePerChunk;

	static {

		if(BetterEnd.isPremium()) {
			main.getLogger().info(LangUtil.enableStructureMessage);
			try {
				config.load(configFile);
				doGeneration = true;
			} catch(IOException e) {
				main.getLogger().warning(LangUtil.structureConfigNotFoundMessage);
			} catch(InvalidConfigurationException e) {
				e.printStackTrace();
			}

			chancePerChunk = config.getInt("master-chance-per-chunk", 6);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
		try {
			if(random.nextInt(100) > chancePerChunk || !doGeneration) return;
			if(!(Math.abs(chunk.getX()) > 20 || Math.abs(chunk.getZ()) > 20 || ConfigUtil.allAether)) return;
			int x = random.nextInt(15);
			int z = random.nextInt(15);
			if(chunk.getX() * 16 + x >= 29999900 || chunk.getZ() * 16 + z >= 29999900) return;

			NMSStructure structure;

			List<Map<?, ?>> structures = config.getMapList("structures");

			IntStream.Builder structureIDs = IntStream.builder();
			IntStream.Builder weights = IntStream.builder();
			for(int i = 0; i < structures.size(); i++) {
				structureIDs.add(i);
				weights.add(((Map<String, Integer>) structures.get(i)).get("weight"));
			}
			Map<?, ?> struc = structures.get(chooseOnWeight(structureIDs.build().toArray(), weights.build().toArray()));

			if(!((List<?>) struc.get("biomes"))
					.contains(Biome.fromCoordinates(chunk.getX() * 16 + x, chunk.getZ() * 16 + z, world.getSeed()).toString().toUpperCase())) return;

			int y;

			switch((String) struc.get("generate")) {
				case "GROUND":
					for(y = world.getMaxHeight()
							- 1; new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.GRASS_BLOCK
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.END_STONE
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.DIRT
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.STONE
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.PODZOL
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.COARSE_DIRT
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.GRAVEL
									&& new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z).getBlock().getType() != Material.STONE_SLAB
									&& y > 0; y--);
					y = y + ((Map<String, Integer>) struc).get("y-offset");
					break;
				case "AIR":
					y = random.nextInt(((Map<String, Integer>) struc).get("max-height") - ((Map<String, Integer>) struc).get("min-height") + 1) + ((Map<String, Integer>) struc).get("min-height");
					break;
				default:
					main.getLogger().warning(String.format(LangUtil.invalidSpawn, struc.get("spawn")));
					return;
			}
			Location origin = new Location(world, (double) chunk.getX() * 16 + x, y, (double) chunk.getZ() * 16 + z);

			boolean ground = false;

			structure = new NMSStructure(origin, new FileInputStream(main.getDataFolder() + "/structures/" + struc.get("name") + ".nbt"));
			if(((Map<String, Boolean>) struc).get("override-checks") || StructureUtil.isValidSpawn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1], ground,
					((Map<String, Boolean>) struc).get("strict-check"))) {
				main.getLogger().info(String.format(LangUtil.generateCustomStructureMessage, struc.get("name"), chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z));
				structure.paste();
				LootTable table = new LootTable((String) struc.get("name"));
				for(Location location: StructureUtil.getChestsIn(structure.getBoundingLocations()[0], structure.getBoundingLocations()[1])) {
					if(location.getBlock().getState() instanceof Container && ((Map<String, Boolean>) struc).get("populate-loot")) {
						table.populateChest(location, random);
					}
				}
			}
		} catch(NullPointerException e) {
			main.getLogger().warning(LangUtil.structureErrorMessage);
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			main.getLogger().warning(LangUtil.structureFileNotFoundMessage);
		}

	}

	public static int chooseOnWeight(int[] items, int[] weights) {
		double completeWeight = 0.0;
		for(int weight: weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for(int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if(countWeight >= r) return items[i];
		}
		return -1;
	}

}
