package com.dfsek.betterend.population.structures;

import org.polydev.gaea.math.ProbabilityCollection;

public class StructureProbabilityUtil {
    public static ProbabilityCollection<Structure> END_STRUCTURES = new ProbabilityCollection<Structure>()
            .add(Structure.END_RUIN, 75)
            .add(Structure.END_HOUSE, 10)
            .add(Structure.END_TOWER, 6)
            .add(Structure.END_SHIP, 2)
            .add(Structure.STRONGHOLD, 7);
    public static ProbabilityCollection<Structure> SHATTERED_END_STRUCTURES = new ProbabilityCollection<Structure>()
            .add(Structure.END_RUIN, 85)
            .add(Structure.END_SHIP, 5)
            .add(Structure.STRONGHOLD, 10);
    public static ProbabilityCollection<Structure> AETHER_STRUCTURES = new ProbabilityCollection<Structure>()
            .add(Structure.AETHER_RUIN, 75)
            .add(Structure.WOOD_HOUSE, 11)
            .add(Structure.COBBLE_HOUSE, 11)
            .add(Structure.GOLD_DUNGEON, 3);
    public static ProbabilityCollection<Structure> AETHER_HIGHLAND_STRUCTURES = new ProbabilityCollection<Structure>()
            .add(Structure.AETHER_RUIN, 75)
            .add(Structure.SPRUCE_WOOD_HOUSE, 11)
            .add(Structure.COBBLE_HOUSE, 11)
            .add(Structure.GOLD_DUNGEON, 3);
    public static ProbabilityCollection<Structure> STARFIELD_STRUCTURES = new ProbabilityCollection<Structure>()
            .add(Structure.VOID_STAR, 100);
}
