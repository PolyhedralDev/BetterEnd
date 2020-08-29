package com.dfsek.betterend.population.structures;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum Structure {
    AETHER_RUIN("aether_ruin", 18, Collections.singletonList(StructureFeatures.COBWEBS), SpawnRequirement.GROUND),
    COBBLE_HOUSE("cobble_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT),
    END_HOUSE("end_house", 3, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT),
    END_RUIN("end_ruin", 109, Collections.emptyList(), SpawnRequirement.GROUND),
    END_SHIP("end_ship", 8, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.TNT_DISPENSER), SpawnRequirement.SKY),
    END_TOWER("end_tower", 2, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT),
    SHULKER_NEST("shulker_nest", 2, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.SHULKERS), SpawnRequirement.GROUND_STRICT),
    SPRUCE_WOOD_HOUSE("spruce_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT),
    STRONGHOLD("stronghold", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.UNDERGROUND),
    VOID_STAR("void_star", 4, Collections.emptyList(), SpawnRequirement.SKY_RANGE),
    GOLD_DUNGEON("gold_dungeon", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.SKY),
    WOOD_HOUSE("wood_house", 45, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT),
    WRECKED_END_SHIP("wrecked_end_ship", 8, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT);

    private final int permutations;
    private final String filename;
    private final List<StructureFeatures> features;
    private final SpawnRequirement spawn;

    Structure(String filename, int permutations, List<StructureFeatures> features, SpawnRequirement spawn) {
        this.permutations = permutations;
        this.filename = filename;
        this.features = features;
        this.spawn = spawn;
    }

    public NMSStructure getInstance(Location origin, Random random) {
        return new NMSStructure(origin, filename, random.nextInt(permutations));
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
    public SpawnRequirement getSpawnRequirement() {
        return this.spawn;
    }
}
