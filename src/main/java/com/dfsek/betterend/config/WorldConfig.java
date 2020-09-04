package com.dfsek.betterend.config;

import com.dfsek.betterend.world.EndBiome;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorldConfig {
    private static final Map<String, WorldConfig> configs = new HashMap<>();
    private static JavaPlugin main;
    private final Map<String, Object> biomeReplacements;
    public boolean endermanBlockPickup;
    public boolean bigTreeSaplingBiomes;
    public boolean bigTreeSaplingWorld;
    public boolean mythicBossEnable;
    public String mythicBossName;
    public boolean fallToOverworldAether;
    public boolean FallToOverworldEverywhere;
    public boolean overworld;
    public long bossRespawnTime;
    public int islandHeightMultiplierTop;
    public int islandHeightMultiplierBottom;
    public int octaves;
    public int noise;
    public int biomeSize;
    public int climateSize;
    public int structureChancePerChunk;
    public boolean genMainIsland;
    public int islandHeight;

    public WorldConfig(String w, JavaPlugin main) {
        WorldConfig.main = main;
        long start = System.nanoTime();
        main.getLogger().info("Loading world configuration values for " + w + "...");
        FileConfiguration config = new YamlConfiguration();
        try {
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w + ".yml");
            if(! configFile.exists() && configFile.getParentFile().mkdirs()) {
                main.getLogger().info("Configuration for world \"" + w + "\" not found. Copying default config.");
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(main.getResource("world.yml")), configFile);
            }
            config.load(configFile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            main.getLogger().severe("Unable to load configuration for world " + w + ".");
        }

        endermanBlockPickup = config.getBoolean("disable-enderman-block-pickup-aether", true);
        bigTreeSaplingBiomes = config.getBoolean("trees.fractal-trees.from-saplings.in-biomes", true);
        bigTreeSaplingWorld = config.getBoolean("trees.fractal-trees.from-saplings.in-world", true);
        mythicBossEnable = config.getBoolean("boss.enable", true);
        mythicBossName = config.getString("boss.gold-name", "SkeletonKing");
        overworld = config.getBoolean("overworld", false);
        bossRespawnTime = Duration.parse(Objects.requireNonNull(config.getString("boss.respawn-time", "P14D"))).toMillis();
        System.out.println("Boss respawn time parsed to " + bossRespawnTime + "ms for " + w);
        islandHeightMultiplierBottom = config.getInt("terrain.height.bottom", 32);
        islandHeightMultiplierTop = config.getInt("terrain.height.top", 6);
        octaves = config.getInt("terrain.noise.octaves", 5);
        noise = config.getInt("terrain.noise.island-size", 96);
        biomeSize = config.getInt("terrain.biomes.size", 1024);
        climateSize = config.getInt("terrain.biome.climate-distribution", 512);
        structureChancePerChunk = config.getInt("structures.chance-per-chunk", 50);
        biomeReplacements = config.getConfigurationSection("terrain.biomes.replacements").getValues(false);
        genMainIsland = config.getBoolean("terrain.main-island", true);
        islandHeight = config.getInt("terrain.ground-level", 64);


        main.getLogger().info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");

        configs.put(w, this);
    }

    public static WorldConfig fromWorld(World w) {
        if(! configs.containsKey(w.getName())) {
            WorldConfig c = new WorldConfig(w.getName(), main);
        }
        return configs.get(w.getName());
    }

    public EndBiome getBiomeReplacement(EndBiome b) {
        if(biomeReplacements.containsKey(b.toString()))
            return EndBiome.valueOf((String) biomeReplacements.get(b.toString()));
        return b;
    }
}
