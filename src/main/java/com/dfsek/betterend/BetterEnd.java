package com.dfsek.betterend;

import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.world.EndChunkGenerator;
import com.dfsek.betterend.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.taskchain.BukkitTaskChainFactory;
import org.polydev.gaea.taskchain.TaskChainFactory;
import org.polydev.gaea.tree.Tree;

import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterEnd extends JavaPlugin {

	public FileConfiguration config = this.getConfig();
	private static BetterEnd instance;
	private TaskChainFactory genChain;
	@Override
	public void onEnable() {
		instance = this;
		genChain = BukkitTaskChainFactory.create(this);
		final Logger logger = this.getLogger();
		NMSStructure.load();
		Metrics metrics = new Metrics(this, 7709);
		metrics.addCustomChart(new Metrics.SimplePie("premium", () -> isPremium() ? "Yes" : "No"));

		this.getServer().getPluginManager().registerEvents(new EventListener(), this);
		this.saveDefaultConfig();
		this.getCommand("betterend").setExecutor(new BetterEndCommand(this));

		ConfigUtil.init(logger, this);

		try {
			MythicSpawnsUtil.startSpawnRoutine();
			if(isPremium()) getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
				logger.info("Enabling advancements...");
				EndAdvancementUtil.enable(instance);
			}, 60);
		} catch(NoClassDefFoundError ignored) {}
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

	public TaskChainFactory getFactory() {
		return genChain;
	}

	@Override
	public void onDisable() {
		Util.logForEach(LangUtil.disableMessage, Level.INFO);
	}

	public static BetterEnd getInstance() {
		return instance;
	}

	@Override
	public EndChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
		new WorldConfig(Objects.requireNonNull(worldName), this);
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
