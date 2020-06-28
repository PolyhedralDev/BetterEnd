package com.dfsek.betterend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.EndAdvancementUtil;
import com.dfsek.betterend.util.MythicSpawnsUtil;
import com.dfsek.betterend.util.NMSReflectorUtil;
import com.dfsek.betterend.util.PremiumUtil;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.generation.EndChunkGenerator;

public class Main extends JavaPlugin {	
	public FileConfiguration config = this.getConfig();
	private static Main instance;
	@Override
	public void onEnable() {
		instance = this;
		Logger logger = this.getLogger();
		NMSReflectorUtil.init(logger);
		try {
			MythicSpawnsUtil.startSpawnRoutine();
			if(isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					logger.info("Enabling advancements...");
					EndAdvancementUtil.enable(instance);
				}
			}, 60);
		} catch(NoClassDefFoundError e) {}
		Metrics metrics = new Metrics(this, 7709);
		metrics.addCustomChart(new Metrics.SimplePie("premium", () -> isPremium() ? "Yes" : "No"));
		this.getServer().getPluginManager().registerEvents(new EventListener(), this);
		this.saveDefaultConfig();
		ConfigUtil.init(logger, this);
		logger.info(" ");
		logger.info(" ");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info("BetterEnd would not have been possible without the support of the following people:");
		logger.info("Developers: dfsek and Baer");
		logger.info("Builders: GucciPoochie, sgtmushroom39, Daverono, and Merazmus");
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info(" ");
		logger.info(" ");
		if(!isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				logger.info(ChatColor.AQUA + "You're running the free version of BetterEnd. Please consider purchasing the premium version to support the plugin and gain additional features!");
				logger.info(ChatColor.AQUA +  "More information can be found here: " + ChatColor.UNDERLINE + "https://github.com/dfsek/BetterEnd-Public/wiki/Premium");
			}
		}, 120);
		if(ConfigUtil.DO_UPDATE_CHECK) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					Util.checkUpdates();
				}
			}, 100, 20L * ConfigUtil.UPDATE_CHECK_FREQUENCY);
		}
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Thank you for using BetterEnd!");
	}

	public static Main getInstance() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("biome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (sender.hasPermission("betterend.checkbiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "You are standing in \"" + ChatColor.DARK_AQUA + Biome.fromLocation(p.getLocation()).toString() + "\"");
				else sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			if (p.hasPermission("betterend.gotobiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) return Util.tpBiome(p, args);
				else sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "This world is not a BetterEnd world!");
				return true;
			} else {
				sender.sendMessage(ChatColor.DARK_AQUA +  "[BetterEnd] " + ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " This server is running " + ChatColor.DARK_AQUA + "BetterEnd v" + this.getDescription().getVersion());
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " Reloading BetterEnd Config...");
			ConfigUtil.loadConfig(this.getLogger(), this);
			sender.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " Complete.");
			return true;
		}
		return false;
	} 

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> COMMANDS = Arrays.asList("biome", "tpbiome", "version", "reload");
		List<String> BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "VOID", "STARFIELD");
		if(isPremium()) {
			BIOMES = Arrays.asList("AETHER", "END", "SHATTERED_END", "AETHER_HIGHLANDS", "SHATTERED_FOREST", "AETHER_FOREST", "AETHER_HIGHLANDS_FOREST", "VOID", "STARFIELD");
		}
		List<String> argList = new ArrayList<>();
		if (args.length == 1) {
			argList.addAll(COMMANDS);
			return argList.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
		}
		if (args.length == 2) {
			switch(args[0]) {
			case "tpbiome":
				argList.addAll(BIOMES);
				return argList.stream().filter(a -> a.startsWith(args[1].toUpperCase())).collect(Collectors.toList());
			default:
			}
		}
		return Arrays.asList(""); // returns an empty list
	}

	@Override
	public EndChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new EndChunkGenerator();
	}

	public static boolean isPremium() {
		try {
			return PremiumUtil.isPremium();
		} catch(NoClassDefFoundError e) {
			return false;
		}
	}
}
