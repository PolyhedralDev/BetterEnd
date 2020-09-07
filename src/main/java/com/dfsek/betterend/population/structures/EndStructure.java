package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.premium.CustomStructuresUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.structures.features.EntityFeature;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.features.LootFeature;
import org.polydev.gaea.structures.features.PersistentDataFeature;
import org.polydev.gaea.structures.spawn.AirSpawn;
import org.polydev.gaea.structures.spawn.GroundSpawn;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;
import org.polydev.gaea.structures.spawn.UndergroundSpawn;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public enum EndStructure implements Structure {
    AETHER_RUIN("aether_ruin", 18, Collections.emptyList(), new GroundSpawn(0)),
    COBBLE_HOUSE("cobble_house", 5, Collections.singletonList(new LootFeature(getLoot("cobble_house.json"))), new GroundSpawn(0)),
    END_HOUSE("end_house", 3, Collections.singletonList(new LootFeature(getLoot("end_house.json"))), new GroundSpawn(- 5)),
    END_RUIN("end_ruin", 109, Collections.emptyList(), new GroundSpawn(0)),
    END_SHIP("end_ship", 8, Collections.singletonList(new LootFeature(getLoot("end_ship.json"))), new AirSpawn(128, 32)),
    END_TOWER("end_tower", 2, Collections.singletonList(new LootFeature(getLoot("end_tower.json"))), new GroundSpawn(0)),
    SHULKER_NEST("shulker_nest", 2, Arrays.asList(new LootFeature(getLoot("shulker_nest.json")), new EntityFeature(2, 6, EntityType.SHULKER)), new GroundSpawn(0)),
    SPRUCE_WOOD_HOUSE("spruce_house", 5, Collections.singletonList(new LootFeature(getLoot("spruce_house.json"))), new GroundSpawn(- 1)),
    STRONGHOLD("stronghold", 1, Collections.singletonList(new LootFeature(getLoot("stronghold.json"))), new UndergroundSpawn(16)),
    VOID_STAR("void_star", 4, Collections.emptyList(), new AirSpawn(128, 250)),
    GOLD_DUNGEON("gold_dungeon", 1, Arrays.asList(new LootFeature(getLoot("gold_dungeon.json")), new PersistentDataFeature(new NamespacedKey(BetterEnd.getInstance(), "valkyrie-spawner"))), new AirSpawn(96, 0)),
    WOOD_HOUSE("wood_house", 45, Collections.singletonList(new LootFeature(getLoot("wood_house.json"))), new GroundSpawn(- 1)),
    WRECKED_END_SHIP("wrecked_end_ship", 8, Collections.singletonList(new LootFeature(getLoot("wrecked_end_ship.json"))), new GroundSpawn(- 5));

    private final int permutations;
    private final String filename;
    private final List<Feature> features;
    private final StructureSpawnInfo spawn;
    private final Map<String, Object> structures = new HashMap<>();

    EndStructure(String filename, int permutations, List<Feature> features, StructureSpawnInfo spawn) {
        this.permutations = permutations;
        this.filename = filename;
        this.features = features;
        this.spawn = spawn;
        for(int i = 0; i < permutations; i++) {
            if(ConfigUtil.debug) Bukkit.getLogger().info("Loading structures to memory: " + filename + i);
            long l = System.nanoTime();
            this.structures.put(filename + i, NMSStructure.getAsTag(EndStructure.class.getResourceAsStream("/structures" + "/" + filename + "/" + filename + "_" + i + ".nbt")));
            if(ConfigUtil.debug) Bukkit.getLogger().info("Took " + (double) (System.nanoTime() - l) / 1000000 + "ms");
        }
    }

    private static InputStream getLoot(String name) {
        try {
            return CustomStructuresUtil.getLootTable(name);
        } catch(FileNotFoundException | NoClassDefFoundError e) {
            return EndStructure.class.getResourceAsStream("/loot/" + name);
        }
    }

    /**
     * Use to load structures at startup.
     */
    public static void init() {
    }

    @Override
    public NMSStructure getInstance(Location origin, Random random) {
        return new NMSStructure(origin, structures.get(filename + random.nextInt(permutations)));
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public StructureSpawnInfo getSpawnInfo() {
        return this.spawn;
    }
}
