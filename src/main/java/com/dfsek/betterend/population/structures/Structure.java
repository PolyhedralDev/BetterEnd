package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.spawn.AirSpawn;
import org.polydev.gaea.structures.spawn.GroundSpawn;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;
import org.polydev.gaea.structures.spawn.UndergroundSpawn;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum Structure {
    AETHER_RUIN("aether_ruin", 18, Collections.singletonList(StructureFeatures.COBWEBS), new GroundSpawn(0)),
    COBBLE_HOUSE("cobble_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), new GroundSpawn(0)),
    END_HOUSE("end_house", 3, Collections.singletonList(StructureFeatures.LOOT_CHEST),new GroundSpawn(-6)),
    END_RUIN("end_ruin", 109, Collections.emptyList(), new GroundSpawn(0)),
    END_SHIP("end_ship", 8, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.TNT_DISPENSER), new AirSpawn(128, 32)),
    END_TOWER("end_tower", 2, Collections.singletonList(StructureFeatures.LOOT_CHEST), new GroundSpawn(0)),
    SHULKER_NEST("shulker_nest", 2, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.SHULKERS), new GroundSpawn(-2)),
    SPRUCE_WOOD_HOUSE("spruce_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), new GroundSpawn(-1)),
    STRONGHOLD("stronghold", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), new UndergroundSpawn(16)),
    VOID_STAR("void_star", 4, Collections.emptyList(), new AirSpawn(128, 250)),
    GOLD_DUNGEON("gold_dungeon", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), new AirSpawn(96, 0)),
    WOOD_HOUSE("wood_house", 45, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), new GroundSpawn(-1)),
    WRECKED_END_SHIP("wrecked_end_ship", 8, Collections.singletonList(StructureFeatures.LOOT_CHEST), new GroundSpawn(-5));

    private final int permutations;
    private final String filename;
    private final List<StructureFeatures> features;
    private final StructureSpawnInfo spawn;

    Structure(String filename, int permutations, List<StructureFeatures> features, StructureSpawnInfo spawn) {
        this.permutations = permutations;
        this.filename = filename;
        this.features = features;
        this.spawn = spawn;
    }

    public NMSStructure getInstance(Location origin, Random random) {
        return new NMSStructure(origin, BetterEnd.getInstance().getResource("structures" + File.separator + filename + File.separator + filename + "_" + random.nextInt(permutations) + ".nbt"));
    }
    public boolean shouldGenerateShulkers() {
        return features.contains(StructureFeatures.SHULKERS);
    }
    public boolean shouldGenerateCobwebs() {
        return features.contains(StructureFeatures.COBWEBS);
    }
    public boolean hasLoot() {
        return features.contains(StructureFeatures.LOOT_CHEST);
    }
    public boolean hasTNTDispensers() {
        return features.contains(StructureFeatures.TNT_DISPENSER);
    }
    public StructureSpawnInfo getSpawnInfo() {
        return this.spawn;
    }
}
