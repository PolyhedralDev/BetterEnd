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

import com.dfsek.betterend.BetterEnd;

public class LangUtil {
	private static final BetterEnd main = BetterEnd.getInstance();
	public static List<String> enableMessage;
	public static List<String> disableMessage;
	public static List<String> freeVersionMessage;
	public static String prefix;
	public static String newVersion;
	public static String upToDate;
	public static String moreRecent;
	public static String updateError;
	public static String playersOnly;
	public static String notBetterEndWorld;
	public static String noPermission;
	public static String versionCommand;
	public static String reloadConfig;
	public static String completeMessage;
	public static String biomeCommand;
	public static String enableStructureMessage;
	public static String enableMythicMobsMessage;
	public static String generateStructureMessage;
	public static String generateCustomStructureMessage;
	public static String invalidSpawn;
	public static String structureErrorMessage;
	public static String structureFileNotFoundMessage;
	public static String structureConfigNotFoundMessage;
	public static String mythicMobsConfigNotFoundMessage;
	public static String mythicMobsFailToSpawnMessage;
	public static String advancementEnableMessage;
	public static List<String> usePaperMessage;
	public static List<String> untestedServerMessage;
	public static String unableToLocateMessage;
	public static String teleportingMessage;
	public static String locatingBiomeMessage;

	private LangUtil() {
	}

	public static void loadlang(String id, Logger logger) {
		File file = new File(main.getDataFolder(), "lang");
		try(JarFile jar = new JarFile(new File(BetterEnd.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
			Util.copyResourcesToDirectory(jar, "lang", file.toString());
		} catch(IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		logger.info("Loading language " + id);
		YamlConfiguration config = new YamlConfiguration();
		File configFile = new File(main.getDataFolder() + File.separator + "lang" + File.separator + id + ".yml");
		try {
			config.load(configFile);
		} catch(IOException e) {
			main.getLogger().severe("Unable to load " + file.toString() + ". Defaulting to language en_us.");
			try {
				config.load(new File(main.getDataFolder() + File.separator + "lang" + File.separator + "en_us.yml"));
			} catch(IOException | InvalidConfigurationException e1) {
				e1.printStackTrace();
				return;
			}
		} catch(InvalidConfigurationException e) {
			e.printStackTrace();
			return;
		}

		enableMessage = config.getStringList("enable");
		disableMessage = config.getStringList("disable");
		freeVersionMessage = config.getStringList("free-notification");
		prefix = ChatColor.translateAlternateColorCodes('&', config.getString("command-prefix", "&3[BetterEnd] "));
		newVersion = ChatColor.translateAlternateColorCodes('&', config.getString("update.new-version", "A new version of BetterEnd is available: %s "));
		upToDate = ChatColor.translateAlternateColorCodes('&', config.getString("update.up-to-date", "Your version of BetterEnd (%s) is up to date!"));
		moreRecent = ChatColor.translateAlternateColorCodes('&',
				config.getString("update.more-recent", "Your version of BetterEnd (%s) is more recent than the one publicly available."));
		updateError = ChatColor.translateAlternateColorCodes('&', config.getString("update.error", "An error occurred while checking for an update. Reason: "));
		playersOnly = ChatColor.translateAlternateColorCodes('&', config.getString("commands.for-players-only", "&bThis command is for players only!"));
		notBetterEndWorld = ChatColor.translateAlternateColorCodes('&', config.getString("commands.not-betterend-world", "&cThis world is not a BetterEnd world!"));
		noPermission = ChatColor.translateAlternateColorCodes('&', config.getString("commands.no-permission", "&cYou do not have permission for this command."));
		versionCommand = ChatColor.translateAlternateColorCodes('&', config.getString("commands.version", "&bThis server is running &3BetterEnd v%s"));
		reloadConfig = ChatColor.translateAlternateColorCodes('&', config.getString("commands.reload-config", "&bReloading BetterEnd Config..."));
		completeMessage = ChatColor.translateAlternateColorCodes('&', config.getString("commands.complete-msg", "&bComplete."));
		biomeCommand = ChatColor.translateAlternateColorCodes('&', config.getString("commands.biome", "&bYou are standing in \"&3%s&b\""));
		enableStructureMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.enable", "Initializing Custom Structure Populator..."));
		enableMythicMobsMessage = ChatColor.translateAlternateColorCodes('&', config.getString("mythicmobs.enable", "Starting MythicMobs integration"));
		generateCustomStructureMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.log", "Generating custom structure \"%1$s\" at %2$s %3$s %4$s."));
		generateStructureMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("structure-log", "Generating structure \"%1$s\" at %2$s %3$s %4$s. Dimensions: X:%5$s, Y:%6$s, Z:%7$s."));
		invalidSpawn = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.invalid-spawn", "%s is an invalid spawn location. Must be either GROUND or AIR."));
		structureErrorMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.error", "An error occurred whilst attempting to generate custom structure"));
		structureFileNotFoundMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.file-not-found", "The requested custom structure file could not be found."));
		structureConfigNotFoundMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("custom-structures.unable-to-find", "Unable to locate customStructures.yml. Aborting custom structure initialization."));
		mythicMobsConfigNotFoundMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("mythicmobs.unable-to-find", "Unable to locate mythicSpawns.yml. Aborting MythicMobs random spawning."));
		mythicMobsFailToSpawnMessage = ChatColor.translateAlternateColorCodes('&',
				config.getString("mythicmobs.fail-to-spawn", "Failed to spawn Mobs. Is MythicMobs installed?"));
		advancementEnableMessage = ChatColor.translateAlternateColorCodes('&', config.getString("advancements.enable", "Enabling advancements..."));
		usePaperMessage = config.getStringList("paper");
		untestedServerMessage = config.getStringList("untested");
		unableToLocateMessage = ChatColor.translateAlternateColorCodes('&', config.getString("commands.unable-to-locate", "&bUnable to locate biome."));
		teleportingMessage = ChatColor.translateAlternateColorCodes('&', config.getString("commands.tp", "&bTeleporting..."));
		locatingBiomeMessage = ChatColor.translateAlternateColorCodes('&', config.getString("commands.locating-biome", "&bLocating biome \"&3%s&b\""));
	}
}
