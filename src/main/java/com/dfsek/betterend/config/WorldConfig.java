package com.dfsek.betterend.config;

import com.dfsek.betterend.population.structures.EndStructure;
import com.dfsek.betterend.world.EndBiome;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.structures.UserDefinedStructure;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorldConfig {
    private static final Map<String, WorldConfig> configs = new HashMap<>();
    private static JavaPlugin main;
    private final String worldName;
    public boolean initialized = false;
    private FileConfiguration config;
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
    private Map<String, Object> biomeReplacements = new HashMap<>();

    public WorldConfig(String w, JavaPlugin main) {
        WorldConfig.main = main;
        this.worldName = w;
        load(w);
    }

    public static void reloadAll(JavaPlugin main) {
        main.getLogger().info("Reloading ALL worlds");
        for(Map.Entry<String, WorldConfig> e: configs.entrySet()) {
            e.getValue().load(e.getKey());
        }
    }

    public static WorldConfig fromWorld(World w) {
        if(!configs.containsKey(w.getName())) {
            configs.put(w.getName(), new WorldConfig(w.getName(), main));
        }
        return configs.get(w.getName());
    }

    public void load(String w) {
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
        this.config = config;
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

        Map<String, UserDefinedStructure> custom = new HashMap<>();
        // Custom structures
        Map<String, Object> customProb = config.getConfigurationSection("structures.custom").getValues(false);
        for(Map.Entry<String, Object> e : customProb.entrySet()) {
            String current = "undefined";
            try {
                String name = e.getKey();
                current = e.getKey();
                if(ConfigUtil.debug) main.getLogger().info("Loading custom structure " + name);
                Map<String, Object> strucConfig = ((ConfigurationSection) e.getValue()).getValues(false);
                String filename = (String) strucConfig.get("name");
                custom.put(name, new UserDefinedStructure(name, new File(main.getDataFolder() + File.separator + "structures" + File.separator + filename)));
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                main.getLogger().severe("No such structure found in custom structures: " + current);
            } catch(ClassCastException ex) {
                main.getLogger().severe("SEVERE structure configuration for: " + current);
            }
        }


        Map<String, Object> prob = config.getConfigurationSection("structures.distribution").getValues(false);

        // Reset all biomes' structure collections.
        for(EndBiome b : EndBiome.values()) {
            b.getDecorator().setStructures(new ProbabilityCollection<>(), w);
        }

        // Redefine structure collections from config
        for(Map.Entry<String, Object> e : prob.entrySet()) { // Iterate over biomes in config
            String current = "undefined";
            try {
                current = e.getKey();
                Biome b = EndBiome.valueOf(e.getKey());
                ProbabilityCollection<Structure> structures = new ProbabilityCollection<>();
                Map<String, Object> strucConfig = ((ConfigurationSection) e.getValue()).getValues(false);
                for(Map.Entry<String, Object> e2 : strucConfig.entrySet()) { // Iterate over structures defined in biome section
                    current = e2.getKey();
                    try {
                        structures.add(EndStructure.valueOf(e2.getKey()), (Integer) e2.getValue());
                    } catch(IllegalArgumentException ex) {
                        if(custom.containsKey(e2.getKey())) structures.add(custom.get(e2.getKey()), (Integer) e2.getValue());
                        else main.getLogger().severe("Unable to locate " + e2.getKey());
                    }
                    if(ConfigUtil.debug) main.getLogger().info("Added " + e2.getKey() + " with probability of " + e2.getValue() + " to " + b.toString() + " Structure list.");
                }
                b.getDecorator().setStructures(structures, w);
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                main.getLogger().severe("No such biome/structure found in structure distributions: " + current);
            } catch(ClassCastException ex) {
                main.getLogger().severe("SEVERE structure configuration for: " + current);
            }
        }



        main.getLogger().info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }

    public EndBiome getBiomeReplacement(EndBiome b) {
        if(biomeReplacements.containsKey(b.toString()))
            return EndBiome.valueOf((String) biomeReplacements.get(b.toString()));
        return b;
    }
}
