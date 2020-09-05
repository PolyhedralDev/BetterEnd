package org.polydev.gaea.structures;

import org.bukkit.Location;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;

import java.util.List;
import java.util.Random;

public class UserDefinedStructure implements Structure {
    @Override
    public NMSStructure getInstance(Location origin, Random r) {
        return null;
    }

    @Override
    public List<Feature> getFeatures() {
        return null;
    }

    @Override
    public StructureSpawnInfo getSpawnInfo() {
        return null;
    }
}
