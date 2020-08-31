package com.dfsek.betterend.population.structures;


import org.bukkit.Chunk;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public interface SpawnChecker {
    boolean isValidSpawn(NMSStructure s);
    int getHeight(int x, int z, Chunk c, Random r, Structure s);
}
