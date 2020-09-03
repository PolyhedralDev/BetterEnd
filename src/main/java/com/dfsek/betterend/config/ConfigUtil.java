package com.dfsek.betterend.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.LangUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtil {
	public static String lang;
	public static boolean debug;
	public static boolean doUpdateCheck;
	public static long updateCheckFrequency;
	public static boolean generateBigTreesEverywhere;
	public static int treeGrowthMultiplier;
	public static boolean parallel;

	private ConfigUtil() {}

	public static void loadConfig(Logger logger, JavaPlugin main) {
		long start = System.nanoTime();
		logger.info("Loading configuration values...");
		main.reloadConfig();
		FileConfiguration config = main.getConfig();

		lang = config.getString("lang", "en_us");
		debug = config.getBoolean("debug", false);
		doUpdateCheck = config.getBoolean("update-checker.enable", true);
		updateCheckFrequency = config.getLong("update-checker.frequency", 3600L);
		generateBigTreesEverywhere = config.getBoolean("big-trees-everywhere", false);
		treeGrowthMultiplier = config.getInt("tree-sapling-growth-modifier", 8);
		parallel = config.getBoolean("parallel", true);


		LangUtil.loadlang(lang, logger);

		logger.info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
	}

	public static void init(Logger logger, BetterEnd main) {
		loadConfig(logger, main);
	}
}
