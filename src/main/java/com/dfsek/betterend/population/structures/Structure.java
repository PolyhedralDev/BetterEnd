package com.dfsek.betterend.population.structures;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum Structure {
    AETHER_RUIN("aether_ruin", 18, Collections.singletonList(StructureFeatures.COBWEBS), SpawnRequirement.GROUND, 0),
    COBBLE_HOUSE("cobble_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT, 0),
    END_HOUSE("end_house", 3, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT, 8),
    END_RUIN("end_ruin", 109, Collections.emptyList(), SpawnRequirement.GROUND, 0),
    END_SHIP("end_ship", 8, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.TNT_DISPENSER), SpawnRequirement.SKY, 32),
    END_TOWER("end_tower", 2, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT, 2),
    SHULKER_NEST("shulker_nest", 2, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.SHULKERS), SpawnRequirement.GROUND_STRICT, 1),
    SPRUCE_WOOD_HOUSE("spruce_house", 5, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT, 3),
    STRONGHOLD("stronghold", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.UNDERGROUND, 8),
    VOID_STAR("void_star", 4, Collections.emptyList(), SpawnRequirement.SKY, 120),
    GOLD_DUNGEON("gold_dungeon", 1, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.SKY, 0),
    WOOD_HOUSE("wood_house", 45, Arrays.asList(StructureFeatures.LOOT_CHEST, StructureFeatures.COBWEBS), SpawnRequirement.GROUND_STRICT, 0),
    WRECKED_END_SHIP("wrecked_end_ship", 8, Collections.singletonList(StructureFeatures.LOOT_CHEST), SpawnRequirement.GROUND_STRICT, 0);

    private final int permutations;
    private final String filename;
    private final List<StructureFeatures> features;
    private final SpawnRequirement spawn;
    private final int offset;

    Structure(String filename, int permutations, List<StructureFeatures> features, SpawnRequirement spawn, int offset) {
        this.permutations = permutations;
        this.filename = filename;
        this.features = features;
        this.spawn = spawn;
        this.offset = offset;
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
    public SpawnRequirement getSpawnRequirement() {
        return this.spawn;
    }
    public int getOffset() {
        return this.offset;
    }
}
