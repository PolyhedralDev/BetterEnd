package com.dfsek.betterend;

import com.dfsek.betterend.command.BetterEndCommand;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.config.LangUtil;
import com.dfsek.betterend.config.WorldConfig;
import com.dfsek.betterend.population.structures.EndStructure;
import com.dfsek.betterend.premium.AetherFallUtil;
import com.dfsek.betterend.premium.EndAdvancementUtil;
import com.dfsek.betterend.premium.MythicSpawnsUtil;
import com.dfsek.betterend.premium.PremiumUtil;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.EndChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.lang.Language;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterEnd extends GaeaPlugin {

    private static BetterEnd instance;
    public FileConfiguration config = this.getConfig();

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
        final Logger logger = this.getLogger();
        NMSStructure.load();

        Metrics metrics = new Metrics(this, 7709);
        metrics.addCustomChart(new Metrics.SimplePie("premium", () -> isPremium() ? "Yes" : "No"));

        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.saveDefaultConfig();

        ConfigUtil.init(logger, this);

        PluginCommand c = Objects.requireNonNull(getCommand("betterend"));
        BetterEndCommand command = new BetterEndCommand(this);
        c.setExecutor(command);
        c.setTabCompleter(command);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, EndChunkGenerator::saveAll, ConfigUtil.dataSave, ConfigUtil.dataSave);
        EndStructure.init();
        try {
            MythicSpawnsUtil.startSpawnRoutine();
            if(isPremium() && ConfigUtil.advancements) getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                logger.info("Enabling advancements...");
                EndAdvancementUtil.enable(instance);
                AetherFallUtil.init(this);
            }, 60);
        } catch(NoClassDefFoundError ignored) {
        }
        logger.info(" ");
        logger.info(" ");
        logger.info("|---------------------------------------------------------------------------------|");
        LangUtil.log("enable", Level.INFO);
        logger.info("|---------------------------------------------------------------------------------|");
        logger.info(" ");
        logger.info(" ");
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            if(! isPremium()) LangUtil.log("free-notification", Level.INFO);
            if(ConfigUtil.debug) logger.info("Server Implementation Name:  " + Bukkit.getServer().getName());
            if("Spigot".equals(Bukkit.getServer().getName())
                    || "CraftBukkit".equals(Bukkit.getServer().getName()))
                LangUtil.log("paper", Level.WARNING);
            else if(! "Paper".equals(Bukkit.getServer().getName()))
                LangUtil.log("untested", Level.WARNING);
        }, 120);
        if(ConfigUtil.doUpdateCheck) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, Util::checkUpdates, 100, 20L * ConfigUtil.updateCheckFrequency);
        }

    }

    @Override
    public void onDisable() {
        EndChunkGenerator.saveAll();
        LangUtil.log("disable", Level.INFO);
    }

    @Override
    public EndChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        new WorldConfig(Objects.requireNonNull(worldName), this);
        return new EndChunkGenerator(worldName);
    }

    @Override
    public boolean isDebug() {
        return ConfigUtil.debug;
    }

    @Override
    public Class<? extends GaeaChunkGenerator> getGeneratorClass() {
        return EndChunkGenerator.class;
    }

    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }
}
