package com.dfsek.betterend.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.dfsek.betterend.Main;

public class ConfigUtil {
	public static int shulkerSpawns;
	public static boolean allAether;
	public static int structureChance;
	public static int ruinChance;
	public static int cloudHeight;
	public static int biomeSize;
	public static int islandHeight;
	public static int[] aetherStructureWeights;
	public static int[] endStructureWeights;
	public static boolean enableMythicBoss;
	public static int outerEndNoise;
	public static boolean doClouds;
	public static boolean doAetherCaveDec;
	public static boolean doEndCaveDec;
	public static boolean preventEndermanPickup;
	public static int cloudNoise;
	public static int heatNoise;
	public static int climateNoise;
	public static int islandHeightMultiplierTop;
	public static int islandHeightMultiplierBottom;
	public static double landPercent;
	public static int maxTrees;
	public static int minTrees;
	public static int herdChance;
	public static int maxHerdSize;
	public static int minHerdSize;
	public static int maxObsidianPillarHeight;
	public static int minObsidianPillarHeight;
	public static boolean doOresAether;
	public static int oreChanceAether;
	public static boolean debug;
	public static long bossRespawnTime;
	public static String goldBossName;
	public static boolean overworld;
	public static int[] oreChances;
	public static boolean doUpdateCheck;
	public static int updateCheckFrequency;
	public static String lang;
	public static boolean fallToOverworld;
	public static boolean fallToOverworldAether;
	public static boolean generateBigTreesInBiomes;
	public static boolean generateBigTreesInEnd;
	public static boolean generateBigTreesEverywhere;
	public static int treeGrowthMultiplier;

	private ConfigUtil() {
	}

	public static void loadConfig(Logger logger, Main main) {
		long start = System.nanoTime();
		logger.info("Loading configuration values...");
		main.reloadConfig();
		FileConfiguration config = main.getConfig();
		shulkerSpawns = config.getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts", 8);
		allAether = config.getBoolean("all-aether", false);
		structureChance = config.getInt("outer-islands.structures.chance-per-chunk", 6);
		ruinChance = config.getInt("outer-islands.ruins.chance-per-chunk", 30);
		cloudHeight = config.getInt("aether.clouds.cloud-height", 128);
		biomeSize = config.getInt("outer-islands.biome-size");
		islandHeight = config.getInt("outer-islands.island-height", 64);
		aetherStructureWeights = new int[]{config.getInt("structure-weight.aether.gold_dungeon", 2), config.getInt("structure-weight.aether.cobble_house", 49),
				config.getInt("structure-weight.aether.wood_house", 49)};
		endStructureWeights = new int[]{config.getInt("structure-weight.end.end_house", 32), config.getInt("structure-weight.end.shulker_nest", 19),
				config.getInt("structure-weight.end.stronghold", 19), config.getInt("structure-weight.end.end_ship", 6),
				config.getInt("structure-weight.end.end_tower", 19), config.getInt("structure-weight.aether.wrecked_end_ship", 19)};
		enableMythicBoss = config.getBoolean("aether.mythic-boss.enable", false);
		outerEndNoise = config.getInt("outer-islands.noise", 56);
		doClouds = config.getBoolean("aether.clouds.enable-clouds", true);
		doAetherCaveDec = config.getBoolean("aether.cave-decoration", true);
		doEndCaveDec = config.getBoolean("outer-islands.cave-decoration", true);
		preventEndermanPickup = config.getBoolean("prevent-enderman-block-pickup", true);
		cloudNoise = config.getInt("aether.clouds.cloud-noise", 36);
		heatNoise = config.getInt("outer-islands.heat-noise", 512);
		climateNoise = config.getInt("outer-islands.climate-noise", 384);
		islandHeightMultiplierTop = config.getInt("outer-islands.height-multiplier.top", 6);
		islandHeightMultiplierBottom = config.getInt("outer-islands.height-multiplier.bottom", 52);
		landPercent = 1 - ((config.getInt("outer-islands.island-threshold", 30)) / 50D);
		minTrees = config.getInt("trees.min-per-chunk", 4);
		maxTrees = config.getInt("trees.max-per-chunk", 7);
		herdChance = config.getInt("aether.animals.herd-chance-per-chunk", 15);
		minHerdSize = config.getInt("aether.animals.herd-min-size", 2);
		maxHerdSize = config.getInt("aether.animals.herd-max-size", 5);
		maxObsidianPillarHeight = config.getInt("trees.obsidian-pillars.max-height");
		minObsidianPillarHeight = config.getInt("trees.obsidian-pillars.min-height");
		doOresAether = config.getBoolean("aether.ores.enable-ores", true);
		oreChanceAether = config.getInt("aether.ores.ore-chance", 20);
		bossRespawnTime = (long) (config.getInt("aether.mythic-boss.respawn-time", 14) * 24 * 60 * 60 * 1000);
		goldBossName = config.getString("aether.mythic-boss.gold-name", "SkeletonKing");
		overworld = config.getBoolean("overworld", false);
		debug = main.config.getBoolean("debug");
		oreChances = new int[]{config.getInt("aether.ores.weight.coal_ore", 40), config.getInt("aether.ores.weights.iron_ore", 25),
				config.getInt("aether.ores.weights.gold_ore", 10), config.getInt("aether.ores.weights.redstone_ore", 10),
				config.getInt("aether.ores.weights.lapis_ore", 10), config.getInt("aether.ores.weights.diamond_ore", 3),
				config.getInt("aether.ores.weights.emerald_ore", 2)};
		doUpdateCheck = config.getBoolean("update-checker.enable", true);
		updateCheckFrequency = config.getInt("update-checker.frequency", 3600);
		lang = config.getString("lang", "en_us");
		fallToOverworld = config.getBoolean("outer-islands.fall-to-overworld", false);
		fallToOverworldAether = config.getBoolean("aether.fall-to-overworld", true);
		generateBigTreesInBiomes = config.getBoolean("trees.big-trees.saplings.in-respective-biomes", true);
		generateBigTreesInEnd = config.getBoolean("trees.big-trees.saplings.in-all-betterend-worlds", false);
		generateBigTreesEverywhere = config.getBoolean("trees.big-trees.saplings.everywhere", false);
		treeGrowthMultiplier = config.getInt("trees.big-trees.saplings.growth-time-multiplier", 8);

		LangUtil.loadlang(lang, logger);
		logger.info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
	}

	public static void init(Logger logger, Main main) {
		FileConfiguration config = main.getConfig();
		if(!Objects.equals(config.getString("config-version", "null"), main.getDescription().getVersion())) {
			logger.info("Updating config...");
			backupConfig(main);
			File configBackupFile = new File(main.getDataFolder() + File.separator + "config.v" + main.getDescription().getVersion() + ".yml");
			YamlConfiguration configBackup = new YamlConfiguration();
			YamlConfiguration configDefault = new YamlConfiguration();
			try(FileOutputStream writer = new FileOutputStream(new File(main.getDataFolder() + File.separator + "default.yml"));) {
				configBackup.load(configBackupFile);
				File configFile = new File(main.getDataFolder() + File.separator + "config.yml");
				if(configFile.delete()) main.getLogger().info("Old config was succesfully deleted.");
				main.saveDefaultConfig();

				InputStream out = main.getResource("config.yml");
				byte[] linebuffer = new byte[4096];
				int lineLength = 0;
				while ((lineLength = out.read(linebuffer)) > 0) {
					writer.write(linebuffer, 0, lineLength);
				}
				File configDefaultFile = new File(main.getDataFolder() + File.separator + "default.yml");
				configBackup.load(configBackupFile);
				configDefault.load(configDefaultFile);
				main.saveConfig();
				config = main.getConfig();
				main.saveConfig();
				for(String key: configDefault.getKeys(true)) {
					if(configBackup.get(key) == null) config.set(key, configDefault.get(key));
					else config.set(key, configBackup.get(key));
				}
				config.set("config-version", main.getDescription().getVersion());
				main.saveConfig();

			} catch(IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		loadConfig(logger, main);

	}

	public static int getStructureWeight(String structure) {
		return Main.getInstance().getConfig().getInt("structure-weight.aether." + structure, 1);
	}

	private static void backupConfig(Main main) {
		File inFile = new File(main.getDataFolder() + File.separator + "config.yml");
		File outFile = new File(main.getDataFolder() + File.separator + "config.v" + main.getDescription().getVersion() + ".yml");
		try(FileInputStream inStream = new FileInputStream(inFile); FileOutputStream outStream = new FileOutputStream(outFile)) {
			if(outFile.createNewFile()) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inStream.read(buffer)) > 0) {
					outStream.write(buffer, 0, length);
				}
				main.getLogger().info("Config backed up successfully.");
			} else {
				main.getLogger().severe("Failed to back up config!");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
