package com.dfsek.betterend.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.dfsek.betterend.Main;

public class LangUtil {
	private static Main main = Main.getInstance();
	public static List<String> ENABLE_MESSAGE;
	public static List<String> DISABLE_MESSAGE;
	public static List<String> FREE_NOTIFICATION;
	public static String PREFIX;
	public static String NEW_VERSION;
	public static String UP_TO_DATE;
	public static String MORE_RECENT;
	public static String UPDATE_ERROR;
	public static String PLAYERS_ONLY;
	public static String NOT_BETTEREND_WORLD;
	public static String NO_PERMISSION;
	public static String VERSION_COMMAND;
	public static String RELOAD_CONFIG;
	public static String COMPLETE_MSG;
	public static String BIOME_COMMAND;
	public static String ENABLE_STRUCTURES;
	public static String ENABLE_MM;
	public static String STRUCTURE_MSG;
	public static String CUSTOM_STRUCTURE_MSG;
	public static String INVALID_SPAWN;
	public static String STRUCTURE_ERROR;
	public static String FILE_NOT_FOUND;
	public static String STRUCTURE_CONFIG_NOT_FOUND;
	public static String MM_CONFIG_NOT_FOUND;
	public static String MM_FAIL_TO_SPAWN;
	public static void loadLang(String id, Logger logger) {
		File file = new File(main.getDataFolder(), "lang");
		try {
			Util.copyResourcesToDirectory(new JarFile(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())), "lang", file.toString());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		logger.info("Loading language " + id);
		YamlConfiguration config = new YamlConfiguration();
		File configFile = new File(main.getDataFolder() + File.separator + "lang" + File.separator + id + ".yml");
		try {
			config.load(configFile);
		} catch (IOException e) {
			main.getLogger().severe("Unable to load " + file.toString() + ". Defaulting to language en_us.");
			try {
				config.load(new File(main.getDataFolder() + File.separator + "lang" + File.separator + "en_us.yml"));
			} catch (IOException | InvalidConfigurationException e1) {
				e1.printStackTrace();
				return;
			}
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return;
		}
		
		ENABLE_MESSAGE = config.getStringList("enable");
		DISABLE_MESSAGE = config.getStringList("disable");
		FREE_NOTIFICATION = config.getStringList("free-notification");
		PREFIX = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("command-prefix"));
		NEW_VERSION = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("update.new-version"));
		UP_TO_DATE = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("update.up-to-date"));
		MORE_RECENT = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("update.more-recent"));
		UPDATE_ERROR = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("update.error"));
		PLAYERS_ONLY = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.for-players-only"));
		NOT_BETTEREND_WORLD = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.not-betterend-world"));
		NO_PERMISSION = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.no-permission"));
		VERSION_COMMAND = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.version"));
		RELOAD_CONFIG = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.reload-config"));
		COMPLETE_MSG = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.complete-msg"));
		BIOME_COMMAND = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("commands.biome"));
		ENABLE_STRUCTURES = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.enable"));
		ENABLE_MM = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("mythicmobs.enable"));
		CUSTOM_STRUCTURE_MSG = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.log"));
		STRUCTURE_MSG = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("structure-log"));
		INVALID_SPAWN = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.invalid-spawn"));
		STRUCTURE_ERROR = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.error"));
		FILE_NOT_FOUND = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.file-not-found"));
		STRUCTURE_CONFIG_NOT_FOUND = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("custom-structures.unable-to-find"));
		MM_CONFIG_NOT_FOUND = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("mythicmobs.unable-to-find"));
		MM_FAIL_TO_SPAWN = ChatColor.translateAlternateColorCodes((char) ('&'), config.getString("mythicmobs.fail-to-spawn"));
		
	}
}
