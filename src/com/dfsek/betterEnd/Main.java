package com.dfsek.betterend;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.betterend.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.generation.EndChunkGenerator;

public class Main extends JavaPlugin {

	public FileConfiguration config = this.getConfig();
	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		final Logger logger = this.getLogger();

		NMSReflectorUtil.init(logger);

		Metrics metrics = new Metrics(this, 7709);
		metrics.addCustomChart(new Metrics.SimplePie("premium", () -> isPremium() ? "Yes" : "No"));
		this.getServer().getPluginManager().registerEvents(new EventListener(), this);
		this.saveDefaultConfig();
		ConfigUtil.init(logger, this);
		try {
			MythicSpawnsUtil.startSpawnRoutine();
			if(ConfigUtil.fallToOverworld || ConfigUtil.fallToOverworldAether) AetherFallUtil.init(this);
			if(isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
				logger.info("Enabling advancements...");
				EndAdvancementUtil.enable(instance);
			}, 60);
		} catch(NoClassDefFoundError e) {
			// not premium, nothing to do here.
		}
		logger.info(" ");
		logger.info(" ");
		logger.info("|---------------------------------------------------------------------------------|");
		Util.logForEach(LangUtil.enableMessage, Level.INFO);
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info(" ");
		logger.info(" ");
		getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
			if(!isPremium()) Util.logForEach(LangUtil.freeVersionMessage, Level.INFO);
			if(ConfigUtil.debug) logger.info("Server Implementation Name:  " + Bukkit.getServer().getName());
			if("Spigot".equals(Bukkit.getServer().getName())
					|| "CraftBukkit".equals(Bukkit.getServer().getName())) Util.logForEach(LangUtil.usePaperMessage, Level.WARNING);
			else if(!"Paper".equals(Bukkit.getServer().getName())) Util.logForEach(LangUtil.untestedServerMessage, Level.WARNING);
		}, 120);
		if(ConfigUtil.doUpdateCheck) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, Util::checkUpdates, 100, 20L * ConfigUtil.updateCheckFrequency);
		}
		this.getCommand("betterend").setTabCompleter(new TabComplete());
	}

	@Override
	public void onDisable() {
		Util.logForEach(LangUtil.disableMessage, Level.INFO);
	}

	public static Main getInstance() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("biome")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(LangUtil.prefix + LangUtil.playersOnly);
				return true;
			}
			Player p = (Player) sender;
			if(sender.hasPermission("betterend.checkbiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) sender
						.sendMessage(LangUtil.prefix + String.format(LangUtil.biomeCommand, Biome.fromLocation(p.getLocation())));
				else sender.sendMessage(LangUtil.prefix + LangUtil.notBetterEndWorld);
				return true;
			} else {
				sender.sendMessage(LangUtil.prefix + LangUtil.noPermission);
				return true;
			}
		} else if(args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(LangUtil.prefix + LangUtil.playersOnly);
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("betterend.gotobiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) return Util.tpBiome(p, args);
				else sender.sendMessage(LangUtil.prefix + LangUtil.notBetterEndWorld);
				return true;
			} else {
				sender.sendMessage(LangUtil.prefix + LangUtil.noPermission);
				return true;
			}
		} else if(args.length == 1 && args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(LangUtil.prefix + String.format(LangUtil.versionCommand, this.getDescription().getVersion()));
			return true;
		} else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(LangUtil.prefix + LangUtil.reloadConfig);
			ConfigUtil.loadConfig(this.getLogger(), this);
			sender.sendMessage(LangUtil.prefix + LangUtil.completeMessage);
			return true;
		}
		return false;
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
