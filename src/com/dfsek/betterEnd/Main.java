package com.dfsek.betterend;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.EndAdvancementUtil;
import com.dfsek.betterend.util.LangUtil;
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
		LangUtil.loadLang(ConfigUtil.LANG, logger);
		logger.info(" ");
		logger.info(" ");
		logger.info("|---------------------------------------------------------------------------------|");
		Util.logForEach(LangUtil.ENABLE_MESSAGE);
		logger.info("|---------------------------------------------------------------------------------|");
		logger.info(" ");
		logger.info(" ");
		if(!isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				Util.logForEach(LangUtil.FREE_NOTIFICATION);
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
		this.getCommand("betterend").setTabCompleter(new TabComplete());
	}

	@Override
	public void onDisable() {
		Util.logForEach(LangUtil.DISABLE_MESSAGE);
	}

	public static Main getInstance() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("biome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(LangUtil.PREFIX + LangUtil.PLAYERS_ONLY);
				return true;
			}
			Player p = (Player) sender;
			if (sender.hasPermission("betterend.checkbiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) sender.sendMessage(LangUtil.PREFIX + String.format(LangUtil.BIOME_COMMAND, Biome.fromLocation(p.getLocation())));
				else sender.sendMessage(LangUtil.PREFIX + LangUtil.NOT_BETTEREND_WORLD);
				return true;
			} else {
				sender.sendMessage(LangUtil.PREFIX + LangUtil.NO_PERMISSION);
				return true;
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(LangUtil.PREFIX + LangUtil.PLAYERS_ONLY);
				return true;
			}
			Player p = (Player) sender;
			if (p.hasPermission("betterend.gotobiome")) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator) return Util.tpBiome(p, args);
				else sender.sendMessage(LangUtil.PREFIX + LangUtil.NOT_BETTEREND_WORLD);
				return true;
			} else {
				sender.sendMessage(LangUtil.PREFIX + LangUtil.NO_PERMISSION);
				return true;
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(LangUtil.PREFIX + String.format(LangUtil.VERSION_COMMAND, this.getDescription().getVersion()));
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(LangUtil.PREFIX + LangUtil.RELOAD_CONFIG);
			ConfigUtil.loadConfig(this.getLogger(), this);
			sender.sendMessage(LangUtil.PREFIX + LangUtil.COMPLETE_MSG);
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
