package com.dfsek.betterend;

import com.dfsek.betterend.command.BetterEndCommand;
import com.dfsek.betterend.command.TabComplete;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.LangUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.population.structures.EndStructure;
import com.dfsek.betterend.premium.EndAdvancementUtil;
import com.dfsek.betterend.premium.MythicSpawnsUtil;
import com.dfsek.betterend.premium.PremiumUtil;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.EndChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.taskchain.BukkitTaskChainFactory;
import org.polydev.gaea.taskchain.TaskChainFactory;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterEnd extends JavaPlugin {

    private static BetterEnd instance;
    public FileConfiguration config = this.getConfig();
    private TaskChainFactory genChain;

    public static BetterEnd getInstance() {
        return instance;
    }

    public static boolean isPremium() {
        try {
            return PremiumUtil.isPremium();
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }

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
        EndStructure.init();
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
            if(! isPremium()) Util.logForEach(LangUtil.freeVersionMessage, Level.INFO);
            if(ConfigUtil.debug) logger.info("Server Implementation Name:  " + Bukkit.getServer().getName());
            if("Spigot".equals(Bukkit.getServer().getName())
                    || "CraftBukkit".equals(Bukkit.getServer().getName()))
                Util.logForEach(LangUtil.usePaperMessage, Level.WARNING);
            else if(! "Paper".equals(Bukkit.getServer().getName()))
                Util.logForEach(LangUtil.untestedServerMessage, Level.WARNING);
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

    @Override
    public EndChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        new WorldConfig(Objects.requireNonNull(worldName), this);
        return new EndChunkGenerator();
    }
}
