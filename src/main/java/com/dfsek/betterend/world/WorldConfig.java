package com.dfsek.betterend.world;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorldConfig {
    private static final Map<String, WorldConfig> configs = new HashMap<>();
    public int shulkerSpawns;
    public boolean allAether;
    public int structureChance;
    public int ruinChance;
    public int cloudHeight;
    public int biomeSize;
    public int islandHeight;
    public int[] aetherStructureWeights;
    public int[] endStructureWeights;
    public boolean enableMythicBoss;
    public int outerEndNoise;
    public boolean doClouds;
    public boolean doAetherCaveDec;
    public boolean doEndCaveDec;
    public boolean preventEndermanPickup;
    public int cloudNoise;
    public int heatNoise;
    public int climateNoise;
    public int islandHeightMultiplierTop;
    public int islandHeightMultiplierBottom;
    public double landPercent;
    public int maxTrees;
    public int minTrees;
    public int herdChance;
    public int maxHerdSize;
    public int minHerdSize;
    public int maxObsidianPillarHeight;
    public int minObsidianPillarHeight;
    public boolean doOresAether;
    public int oreChanceAether;
    public long bossRespawnTime;
    public String goldBossName;
    public boolean overworld;
    public int[] oreChances;
    public boolean fallToOverworld;
    public boolean fallToOverworldAether;
    public boolean generateBigTreesInBiomes;
    public boolean generateBigTreesInEnd;
    public int treeGrowthMultiplier;
    public WorldConfig(String w, BetterEnd main) {
        long start = System.nanoTime();
        main.getLogger().info("Loading world configuration values for " + w + "...");
        main.reloadConfig();
        FileConfiguration config = new YamlConfiguration();
        try {
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w + ".yml");
            if(!configFile.exists() && configFile.getParentFile().mkdirs()) {
                main.getLogger().info("Configuration for world \"" + w + "\" not found. Copying master config.");
                FileUtils.copyFile(new File(main.getDataFolder(),"config.yml"), configFile);
            }
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            main.getLogger().severe("Unable to load configuration for world " + w + ". Falling back to master config.");
            config = main.getConfig();
        }
        shulkerSpawns = config.getInt("outer-islands.structures.shulker-nest.shulker-spawn-attempts", 8);
        allAether = config.getBoolean("all-aether", false);
        structureChance = config.getInt("outer-islands.structures.chance-per-chunk", 6);
        ruinChance = config.getInt("outer-islands.ruins.chance-per-chunk", 30);
        cloudHeight = config.getInt("aether.clouds.cloud-height", 128);
        biomeSize = config.getInt("outer-islands.biome-size");
        islandHeight = config.getInt("outer-islands.island-height", 64);
        aetherStructureWeights = new int[]{config.getInt("structure-weight.aether.gold_dungeon", 2), config.getInt("structure-weight.aether.cobble_house", 49),
                config.getInt("structure-weight.aether.wood_house", 49)};
        endStructureWeights = new int[]{config.getInt("structure-weight.end.end_house", 32), config.getInt("structure-weight.end.shulker_nest", 19),
                config.getInt("structure-weight.end.stronghold", 19), config.getInt("structure-weight.end.end_ship", 6),
                config.getInt("structure-weight.end.end_tower", 19), config.getInt("structure-weight.aether.wrecked_end_ship", 19)};
        enableMythicBoss = config.getBoolean("aether.mythic-boss.enable", false);
        outerEndNoise = config.getInt("outer-islands.noise", 56);
        doClouds = config.getBoolean("aether.clouds.enable-clouds", true);
        doAetherCaveDec = config.getBoolean("aether.cave-decoration", true);
        doEndCaveDec = config.getBoolean("outer-islands.cave-decoration", true);
        preventEndermanPickup = config.getBoolean("prevent-enderman-block-pickup", true);
        cloudNoise = config.getInt("aether.clouds.cloud-noise", 36);
        heatNoise = config.getInt("outer-islands.heat-noise", 512);
        climateNoise = config.getInt("outer-islands.climate-noise", 384);
        islandHeightMultiplierTop = config.getInt("outer-islands.height-multiplier.top", 6);
        islandHeightMultiplierBottom = config.getInt("outer-islands.height-multiplier.bottom", 52);
        landPercent = 1 - ((config.getInt("outer-islands.island-threshold", 30)) / 50D);
        minTrees = config.getInt("trees.min-per-chunk", 4);
        maxTrees = config.getInt("trees.max-per-chunk", 7);
        herdChance = config.getInt("aether.animals.herd-chance-per-chunk", 15);
        minHerdSize = config.getInt("aether.animals.herd-min-size", 2);
        maxHerdSize = config.getInt("aether.animals.herd-max-size", 5);
        maxObsidianPillarHeight = config.getInt("trees.obsidian-pillars.max-height");
        minObsidianPillarHeight = config.getInt("trees.obsidian-pillars.min-height");
        doOresAether = config.getBoolean("aether.ores.enable-ores", true);
        oreChanceAether = config.getInt("aether.ores.ore-chance", 20);
        bossRespawnTime = (long) (config.getInt("aether.mythic-boss.respawn-time", 14) * 24 * 60 * 60 * 1000);
        goldBossName = config.getString("aether.mythic-boss.gold-name", "SkeletonKing");
        overworld = config.getBoolean("overworld", false);
        oreChances = new int[]{config.getInt("aether.ores.weight.coal_ore", 40), config.getInt("aether.ores.weights.iron_ore", 25),
                config.getInt("aether.ores.weights.gold_ore", 10), config.getInt("aether.ores.weights.redstone_ore", 10),
                config.getInt("aether.ores.weights.lapis_ore", 10), config.getInt("aether.ores.weights.diamond_ore", 3),
                config.getInt("aether.ores.weights.emerald_ore", 2)};
        fallToOverworld = config.getBoolean("outer-islands.fall-to-overworld", false);
        fallToOverworldAether = config.getBoolean("aether.fall-to-overworld", true);
        generateBigTreesInBiomes = config.getBoolean("trees.big-trees.saplings.in-respective-biomes", true);
        generateBigTreesInEnd = config.getBoolean("trees.big-trees.saplings.in-all-betterend-worlds", false);
        treeGrowthMultiplier = config.getInt("trees.big-trees.saplings.growth-time-multiplier", 8);

        main.getLogger().info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");

        configs.put(w, this);
    }
    public static WorldConfig fromWorld(World w) {
        return configs.get(w.getName());
    }

}
