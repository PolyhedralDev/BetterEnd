package com.dfsek.betterend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtil {
	public static int SHULKER_SPAWNS;
	public static boolean ALL_AETHER;
	public static int STRUCTURE_CHANCE;
	public static int RUIN_CHANCE;
	public static int CLOUD_HEIGHT;
	public static int BIOME_SIZE;
	public static int ISLAND_HEIGHT;
	public static int[] AETHER_STRUCTURE_WEIGHTS;
	public static int[] END_STRUCTURE_WEIGHTS;
	public static boolean ENABLE_MYTHIC_BOSS;
	public static int OUTER_END_NOISE;
	public static boolean DO_CLOUDS;
	public static boolean DO_AETHER_CAVE_DEC;
	public static boolean DO_END_CAVE_DEC;
	public static int CLOUD_NOISE;
	public static int HEAT_NOISE;
	public static int ISLAND_HEIGHT_MULTIPLIER_TOP;
	public static int ISLAND_HEIGHT_MULTIPLIER_BOTTOM;
	public static double LAND_PERCENT;
	public static int MAX_TREES;
	public static int MIN_TREES;
	public static int HERD_CHANCE;
	public static int MAX_HERD;
	public static int MIN_HERD;
	public static int OBSIDIAN_PILLAR_MAX_HEIGHT;
	public static int OBSIDIAN_PILLAR_MIN_HEIGHT;
	public static boolean DO_AETHER_ORES;
	public static int AETHER_ORE_CHANCE;
	public static boolean DEBUG;
	
	public static void loadConfig(Logger logger, Main main) {
		long start = System.nanoTime();
		logger.info("Loading configuration values...");
		main.reloadConfig();
		FileConfiguration config = main.getConfig();
		SHULKER_SPAWNS = config.getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts", 8);
		ALL_AETHER = config.getBoolean("all-aether", false);
		STRUCTURE_CHANCE = config.getInt("outer-islands.structures.chance-per-chunk", 6);
		RUIN_CHANCE = config.getInt("outer-islands.ruins.chance-per-chunk", 30);
		CLOUD_HEIGHT = config.getInt("aether.clouds.cloud-height", 128);
		BIOME_SIZE = main.getConfig().getInt("outer-islands.biome-size"); 
		ISLAND_HEIGHT = main.getConfig().getInt("outer-islands.island-height", 64);
		AETHER_STRUCTURE_WEIGHTS = new int[] {config.getInt("structure-weight.aether.gold_dungeon", 2), config.getInt("structure-weight.aether.cobble_house", 49), config.getInt("structure-weight.aether.wood_house", 49)};
		END_STRUCTURE_WEIGHTS = new int[] {config.getInt("structure-weight.end.end_house", 32), config.getInt("structure-weight.end.shulker_nest", 19), config.getInt("structure-weight.end.stronghold", 19), config.getInt("structure-weight.end.end_ship", 6), config.getInt("structure-weight.end.end_tower", 19), config.getInt("structure-weight.aether.wrecked_end_ship", 19)};
		ENABLE_MYTHIC_BOSS = config.getBoolean("aether.mythic-boss.enable", false);
		OUTER_END_NOISE = main.getConfig().getInt("outer-islands.noise", 56);
		DO_CLOUDS = main.getConfig().getBoolean("aether.clouds.enable-clouds", true);
		DO_AETHER_CAVE_DEC = main.getConfig().getBoolean("aether.cave-decoration", true);
		DO_END_CAVE_DEC = main.getConfig().getBoolean("outer-islands.cave-decoration", true);
		CLOUD_NOISE = main.getConfig().getInt("aether.clouds.cloud-noise", 36);
		HEAT_NOISE = main.getConfig().getInt("outer-islands.heat-noise", 512);
		ISLAND_HEIGHT_MULTIPLIER_TOP = main.getConfig().getInt("outer-islands.height-multiplier.top", 6);
		ISLAND_HEIGHT_MULTIPLIER_BOTTOM = main.getConfig().getInt("outer-islands.height-multiplier.bottom", 52);
		LAND_PERCENT = 1-((double) ((main.getConfig().getInt("outer-islands.island-threshold", 30))/50D));
		MIN_TREES = main.getConfig().getInt("trees.min-per-chunk");
		MAX_TREES = main.getConfig().getInt("trees.max-per-chunk");
		HERD_CHANCE = main.getConfig().getInt("aether.animals.herd-chance-per-chunk", 15);
		MIN_HERD = main.getConfig().getInt("aether.animals.herd-min-size", 2);
		MAX_HERD = main.getConfig().getInt("aether.animals.herd-max-size", 5);
		OBSIDIAN_PILLAR_MAX_HEIGHT = main.getConfig().getInt("trees.obsidian-pillars.max-height");
		OBSIDIAN_PILLAR_MIN_HEIGHT = main.getConfig().getInt("trees.obsidian-pillars.min-height");
		DO_AETHER_ORES = main.getConfig().getBoolean("aether.ores.enable-ores");
		AETHER_ORE_CHANCE = main.getConfig().getInt("aether.ores.ore-chance", 20);
		DEBUG = main.config.getBoolean("debug");
		logger.info("Complete. Time elapsed: " + ((double) (System.nanoTime()-start))/1000000 + "ms");
	}
	
	public static void init(Logger logger, Main main) {
		FileConfiguration config = main.getConfig();
		if(!config.getString("config-version", "null").equals(main.getDescription().getVersion())) {
			logger.info("Updating config...");
			backupConfig(main);
			File configBackupFile = new File(main.getDataFolder() + File.separator + "config.v" + main.getDescription().getVersion() + ".yml");
			YamlConfiguration configBackup= new YamlConfiguration();
			YamlConfiguration configDefault= new YamlConfiguration();
			try {
				configBackup.load(configBackupFile);
				File configFile = new File(main.getDataFolder() + File.separator + "config.yml");
				if(configFile.delete()) main.getLogger().info("Old config was succesfully deleted.");
				main.saveDefaultConfig();
				FileOutputStream writer = new FileOutputStream(new File(main.getDataFolder() + File.separator + "default.yml"));
				InputStream out = main.getResource("config.yml");
				byte[] linebuffer = new byte[4096];
				int lineLength = 0;
				while((lineLength = out.read(linebuffer)) > 0) {
					writer.write(linebuffer, 0, lineLength);
				}
				writer.close();
				File configDefaultFile = new File(main.getDataFolder() + File.separator + "default.yml");
				configBackup.load(configBackupFile);
				configDefault.load(configDefaultFile);
				main.saveConfig();
				config = main.getConfig();
				main.saveConfig();
				for(String key : configDefault.getKeys(true)) {
					if(configBackup.get(key) == null) config.set(key, configDefault.get(key));
					else config.set(key, configBackup.get(key));
				}
				config.set("config-version", main.getDescription().getVersion());
				main.saveConfig();

			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		loadConfig(logger, main);
		
	}
	
	private static void backupConfig(Main main) {
		FileInputStream inStream = null;
		FileOutputStream outStream = null;

		try {
			File inFile = new File(main.getDataFolder() + File.separator + "config.yml");
			File outFile = new File(main.getDataFolder() + File.separator + "config.v" + main.getDescription().getVersion() + ".yml");
			outFile.createNewFile();
			inStream = new FileInputStream(inFile);
			outStream = new FileOutputStream(outFile);

			byte[] buffer = new byte[1024];

			int length;
			/*copying the contents from input stream to
			 * output stream using read and write methods
			 */
			while ((length = inStream.read(buffer)) > 0){
				outStream.write(buffer, 0, length);
			}

			//Closing the input/output file streams
			inStream.close();
			outStream.close();

			main.getLogger().info("Config backed up successfully.");

		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
