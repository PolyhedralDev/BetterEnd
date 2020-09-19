package com.dfsek.betterend.config;

import com.dfsek.betterend.population.structures.EndStructure;
import com.dfsek.betterend.premium.CustomStructuresUtil;
import com.dfsek.betterend.world.EndBiome;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.commons.io.FileUtils;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.structures.UserDefinedStructure;
import org.polydev.gaea.world.Ore;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorldConfig {
    private static final Map<String, WorldConfig> configs = new HashMap<>();
    private static JavaPlugin main;
    public boolean initialized = false;
    public boolean endermanBlockPickup;
    public boolean bigTreeSaplingBiomes;
    public boolean bigTreeSaplingWorld;
    public boolean mythicBossEnable;
    public String mythicBossName;
    public boolean fallToOverworldAether;
    public boolean fallToOverworldEverywhere;
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
    public int oreAttempts;
    public boolean enableCaves;
    public int outerRadius;
    public boolean legacyDistribution;
    public Map<EndBiome, ProbabilityCollection<Ore>> ores = new HashMap<>();
    private Map<String, Object> biomeReplacements = new HashMap<>();

    public WorldConfig(String w, JavaPlugin main) {
        WorldConfig.main = main;
        load(w);
    }

    public static void reloadAll(JavaPlugin main) {
        main.getLogger().info("Reloading ALL worlds");
        for(Map.Entry<String, WorldConfig> e : configs.entrySet()) {
            e.getValue().load(e.getKey());
        }
    }

    public static WorldConfig fromWorld(World w) {
        if(! configs.containsKey(w.getName())) {
            configs.put(w.getName(), new WorldConfig(w.getName(), main));
        }
        return configs.get(w.getName());
    }

    public static WorldConfig fromWorld(String w) {
        if(! configs.containsKey(w)) {
            configs.put(w, new WorldConfig(w, main));
        }
        return configs.get(w);
    }

    public void load(String w) {
        long start = System.nanoTime();
        main.getLogger().info("Loading world configuration values for " + w + "...");
        FileConfiguration config = new YamlConfiguration();
        try {
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w + ".yml");
            if(! configFile.exists()) {
                configFile.getParentFile().mkdirs();
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
        if(ConfigUtil.debug) System.out.println("Boss respawn time parsed to " + bossRespawnTime + "ms for " + w);
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
        oreAttempts = config.getInt("ores.attempts", 10);
        enableCaves = config.getBoolean("caves.enable", false);
        outerRadius = config.getInt("terrain.outer-end-radius", 1000);
        legacyDistribution = config.getBoolean("terrain.biomes.legacy-normalization", true);

        fallToOverworldAether = config.getBoolean("fall.fall-to-overworld.enable-aether", true);
        fallToOverworldEverywhere = config.getBoolean("fall.fall-to-overworld.enable-everywhere", true);

        if(legacyDistribution)
            main.getLogger().warning("Enabling legacy biome distribution! Unless you are using a legacy (4.0.x) world, this is a bug!");

        // Define ores
        Map<String, Object> oreBiomes = config.getConfigurationSection("ores.biomes").getValues(false);
        for(Map.Entry<String, Object> biomeEntry : oreBiomes.entrySet()) {
            String current = biomeEntry.getKey();
            try {
                Map<String, Object> oresConfig = ((ConfigurationSection) biomeEntry.getValue()).getValues(false);
                ProbabilityCollection<Ore> oreSet = new ProbabilityCollection<>();
                for(Map.Entry<String, Object> ore : oresConfig.entrySet()) {
                    current = ore.getKey();
                    if(ConfigUtil.debug)
                        main.getLogger().info("Adding ore " + ore.getKey() + " with probability " + ((List<Integer>) ore.getValue()).get(0) + " and vein chance " + ((List<Integer>) ore.getValue()).get(1) + " for biome " + biomeEntry.getKey());
                    oreSet.add(new Ore(Material.valueOf(ore.getKey()).createBlockData(), ((List<Integer>) ore.getValue()).get(1)), ((List<Integer>) ore.getValue()).get(0));
                }
                ores.put(EndBiome.valueOf(biomeEntry.getKey()), oreSet);
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                main.getLogger().severe("No such material/biome found: " + current);
            } catch(ClassCastException ex) {
                main.getLogger().severe("SEVERE Ore configuration error for: " + current);
            }
        }


        // Custom structures
        Map<String, UserDefinedStructure> custom = new HashMap<>();
        try {
            custom = CustomStructuresUtil.getCustomStructures(config.getConfigurationSection("structures.custom").getValues(false), main);
        } catch(NoClassDefFoundError ignored) {
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
                    } catch(IllegalArgumentException ex) { // If no enum type is found, check if a custom structure has been defined.
                        if(custom.containsKey(e2.getKey()))
                            structures.add(custom.get(e2.getKey()), (Integer) e2.getValue());
                        else main.getLogger().severe("Unable to locate " + e2.getKey());
                    }
                    if(ConfigUtil.debug)
                        main.getLogger().info("Added " + e2.getKey() + " with probability of " + e2.getValue() + " to " + b.toString() + " Structure list.");
                }
                b.getDecorator().setStructures(structures, w);
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                main.getLogger().severe("No such biome/structure found in structure distributions: " + current);
            } catch(ClassCastException ex) {
                main.getLogger().severe("SEVERE structure configuration error for: " + current);
            }
        }


        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }

    public EndBiome getBiomeReplacement(EndBiome b) {
        if(biomeReplacements.containsKey(b.toString()))
            return EndBiome.valueOf((String) biomeReplacements.get(b.toString()));
        return b;
    }
}
