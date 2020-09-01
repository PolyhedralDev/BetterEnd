package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.features.LootFeature;
import org.polydev.gaea.structures.spawn.AirSpawn;
import org.polydev.gaea.structures.spawn.GroundSpawn;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;
import org.polydev.gaea.structures.spawn.UndergroundSpawn;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum EndStructure implements Structure {
    AETHER_RUIN("aether_ruin", 18, Collections.emptyList(), new GroundSpawn(0)),
    COBBLE_HOUSE("cobble_house", 5, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/cobble_house.json"))), new GroundSpawn(0)),
    END_HOUSE("end_house", 3, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/end_house.json"))),new GroundSpawn(-6)),
    END_RUIN("end_ruin", 109, Collections.emptyList(), new GroundSpawn(0)),
    END_SHIP("end_ship", 8, Arrays.asList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/end_ship.json"))), new AirSpawn(128, 32)),
    END_TOWER("end_tower", 2, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/end_tower.json"))), new GroundSpawn(0)),
    SHULKER_NEST("shulker_nest", 2, Arrays.asList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/shulker_nest.json"))), new GroundSpawn(-2)),
    SPRUCE_WOOD_HOUSE("spruce_house", 5, Arrays.asList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/spruce_house.json"))), new GroundSpawn(-1)),
    STRONGHOLD("stronghold", 1, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/stronghold.json"))), new UndergroundSpawn(16)),
    VOID_STAR("void_star", 4, Collections.emptyList(), new AirSpawn(128, 250)),
    GOLD_DUNGEON("gold_dungeon", 1, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/gold_dungeon.json"))), new AirSpawn(96, 0)),
    WOOD_HOUSE("wood_house", 45, Arrays.asList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/wood_house.json"))), new GroundSpawn(-1)),
    WRECKED_END_SHIP("wrecked_end_ship", 8, Collections.singletonList(new LootFeature(EndStructure.class.getResourceAsStream("/loot/wrecked_end_ship.json"))), new GroundSpawn(-5));

    private final int permutations;
    private final String filename;
    private final List<Feature> features;
    private final StructureSpawnInfo spawn;

    EndStructure(String filename, int permutations, List<Feature> features, StructureSpawnInfo spawn) {
        this.permutations = permutations;
        this.filename = filename;
        this.features = features;
        this.spawn = spawn;
    }

    @Override
    public NMSStructure getInstance(Location origin, Random random) {
        return new NMSStructure(origin, BetterEnd.getInstance().getResource("structures" + File.separator + filename + File.separator + filename + "_" + random.nextInt(permutations) + ".nbt"));
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
