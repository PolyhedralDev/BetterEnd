package com.dfsek.betterend.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.dfsek.betterend.Main;

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
	public static boolean PREVENT_ENDERMAN_PICKUP;
	public static int CLOUD_NOISE;
	public static int HEAT_NOISE;
	public static int CLIMATE_NOISE;
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
	public static long BOSS_RESPAWN;
	public static String BOSS_NAME_GOLD;
	public static boolean OVERWORLD;
	public static int[] ORE_CHANCES;
	public static boolean DO_UPDATE_CHECK;
	public static int UPDATE_CHECK_FREQUENCY;
	
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
		BIOME_SIZE = config.getInt("outer-islands.biome-size"); 
		ISLAND_HEIGHT = config.getInt("outer-islands.island-height", 64);
		AETHER_STRUCTURE_WEIGHTS = new int[] {config.getInt("structure-weight.aether.gold_dungeon", 2), config.getInt("structure-weight.aether.cobble_house", 49), config.getInt("structure-weight.aether.wood_house", 49)};
		END_STRUCTURE_WEIGHTS = new int[] {config.getInt("structure-weight.end.end_house", 32), config.getInt("structure-weight.end.shulker_nest", 19), config.getInt("structure-weight.end.stronghold", 19), config.getInt("structure-weight.end.end_ship", 6), config.getInt("structure-weight.end.end_tower", 19), config.getInt("structure-weight.aether.wrecked_end_ship", 19)};
		ENABLE_MYTHIC_BOSS = config.getBoolean("aether.mythic-boss.enable", false);
		OUTER_END_NOISE = config.getInt("outer-islands.noise", 56);
		DO_CLOUDS = config.getBoolean("aether.clouds.enable-clouds", true);
		DO_AETHER_CAVE_DEC = config.getBoolean("aether.cave-decoration", true);
		DO_END_CAVE_DEC = config.getBoolean("outer-islands.cave-decoration", true);
		PREVENT_ENDERMAN_PICKUP = config.getBoolean("prevent-enderman-block-pickup", true);
		CLOUD_NOISE = config.getInt("aether.clouds.cloud-noise", 36);
		HEAT_NOISE = config.getInt("outer-islands.heat-noise", 512);
		CLIMATE_NOISE = config.getInt("outer-islands.climate-noise", 384);
		ISLAND_HEIGHT_MULTIPLIER_TOP = config.getInt("outer-islands.height-multiplier.top", 6);
		ISLAND_HEIGHT_MULTIPLIER_BOTTOM = config.getInt("outer-islands.height-multiplier.bottom", 52);
		LAND_PERCENT = 1-((double) ((config.getInt("outer-islands.island-threshold", 30))/50D));
		MIN_TREES = config.getInt("trees.min-per-chunk", 4);
		MAX_TREES = config.getInt("trees.max-per-chunk", 7);
		HERD_CHANCE = config.getInt("aether.animals.herd-chance-per-chunk", 15);
		MIN_HERD = config.getInt("aether.animals.herd-min-size", 2);
		MAX_HERD = config.getInt("aether.animals.herd-max-size", 5);
		OBSIDIAN_PILLAR_MAX_HEIGHT = config.getInt("trees.obsidian-pillars.max-height");
		OBSIDIAN_PILLAR_MIN_HEIGHT = config.getInt("trees.obsidian-pillars.min-height");
		DO_AETHER_ORES = config.getBoolean("aether.ores.enable-ores", true);
		AETHER_ORE_CHANCE = config.getInt("aether.ores.ore-chance", 20);
		BOSS_RESPAWN = (long) (config.getInt("aether.mythic-boss.respawn-time", 14)*24*60*60*1000);
		BOSS_NAME_GOLD = config.getString("aether.mythic-boss.gold-name", "SkeletonKing");
		OVERWORLD = config.getBoolean("overworld", false);
		DEBUG = main.config.getBoolean("debug");
		ORE_CHANCES = new int[] {config.getInt("aether.ores.weight.coal_ore", 40), 
				config.getInt("aether.ores.weights.iron_ore", 25),
				config.getInt("aether.ores.weights.gold_ore", 10),
				config.getInt("aether.ores.weights.redstone_ore", 10),
				config.getInt("aether.ores.weights.lapis_ore", 10),
				config.getInt("aether.ores.weights.diamond_ore", 3),
				config.getInt("aether.ores.weights.emerald_ore", 2)};
		DO_UPDATE_CHECK = config.getBoolean("update-checker.enable", true);
		UPDATE_CHECK_FREQUENCY = config.getInt("update-checker.frequency", 3600);
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
	
	public static int getStructureWeight(String structure) {
		return Main.getInstance().getConfig().getInt("structure-weight.aether." + structure, 1);
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
