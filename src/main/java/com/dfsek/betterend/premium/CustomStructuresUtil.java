package com.dfsek.betterend.premium;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.UserDefinedStructure;
import org.polydev.gaea.structures.features.BlockReplaceFeature;
import org.polydev.gaea.structures.features.EntityFeature;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.features.LootFeature;
import org.polydev.gaea.structures.spawn.AirSpawn;
import org.polydev.gaea.structures.spawn.GroundSpawn;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomStructuresUtil {
    public static FileInputStream getLootTable(String name) throws FileNotFoundException {
        return new FileInputStream(new File(BetterEnd.getInstance().getDataFolder() + File.separator + "loot" + File.separator + name));
    }

    public static Map<String, UserDefinedStructure> getCustomStructures(Map<String, Object> customProb, JavaPlugin main) {
        Map<String, UserDefinedStructure> custom = new HashMap<>();
        for(Map.Entry<String, Object> e : customProb.entrySet()) {
            String current = "undefined";
            try {
                String name = e.getKey();
                current = e.getKey();
                if(ConfigUtil.debug) main.getLogger().info("Loading custom structure " + name);
                Map<String, Object> strucConfig = ((ConfigurationSection) e.getValue()).getValues(false);
                String filename = (String) strucConfig.get("name");
                List<Feature> structureFeatures = new ArrayList<>();
                if(strucConfig.containsKey("features")) {
                    for(Map.Entry<String, Object> feature : ((ConfigurationSection) strucConfig.get("features")).getValues(false).entrySet()) { // Iterate over custom structure features
                        if(ConfigUtil.debug) main.getLogger().info("Loading feature " + feature.getKey());
                        Map<String, Object> f = ((ConfigurationSection) feature.getValue()).getValues(false);
                        switch(feature.getKey()) {
                            case "block_replace":
                                structureFeatures.add(new BlockReplaceFeature((double) f.getOrDefault("percent", 50D), new ProbabilityCollection<Material>().add(Material.valueOf((String) f.get("material")), 1)));
                                break;
                            case "loot":
                                try {
                                    structureFeatures.add(new LootFeature(new FileInputStream(new File(main.getDataFolder() + File.separator + "loot" + File.separator + f.get("name")))));
                                } catch(FileNotFoundException fileNotFoundException) {
                                    main.getLogger().severe("Unable to locate loot table " + f.get("name"));
                                }
                                break;
                            case "spawn_mob":
                                structureFeatures.add(new EntityFeature((int) f.get("min"), (int) f.get("max"), EntityType.valueOf((String) f.get("type"))));
                                break;
                            default:
                                main.getLogger().severe("Invalid feature: " + feature.getKey());
                        }
                    }
                } else {
                    if(ConfigUtil.debug) main.getLogger().info("No features to load. ");
                }
                StructureSpawnInfo spawn = new GroundSpawn(1);
                if(strucConfig.containsKey("spawn")) {
                    Map<String, Object> spawnMap = ((ConfigurationSection) strucConfig.get("spawn")).getValues(false);
                    switch(((String) spawnMap.get("type")).toUpperCase()) {
                        case "GROUND":
                            spawn = new GroundSpawn((int) spawnMap.get("offset"));
                            break;
                        case "AIR":
                            spawn = new AirSpawn((int) spawnMap.get("height"), (int) spawnMap.get("offset"));
                    }
                }

                custom.put(name, new UserDefinedStructure(name, new File(main.getDataFolder() + File.separator + "structures" + File.separator + filename), structureFeatures, spawn));
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                main.getLogger().severe("No such structure found in custom structures: " + current);
            } catch(ClassCastException | NullPointerException ex) {
                ex.printStackTrace();
                main.getLogger().severe("SEVERE structure configuration error for: " + current);
            }
        }
        return custom;
    }
}
