package org.polydev.gaea.structures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.spawn.GroundSpawn;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserDefinedStructure implements Structure {
    private static final Map<UserDefinedStructure, Object> nmsContainer = new HashMap<>();
    private final String id;

    public UserDefinedStructure(String id, File location) {
        this.id = id;
        try {
            nmsContainer.put(this, NMSStructure.getAsTag(new FileInputStream(location)));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("[Gaea] Unable to load UserDefinedStructure " + id + "!");
        }
    }

    @Override
    public NMSStructure getInstance(Location origin, Random r) {
        return new NMSStructure(origin, nmsContainer.get(this));
    }

    @Override
    public List<Feature> getFeatures() {
        return Collections.emptyList();
    }

    @Override
    public StructureSpawnInfo getSpawnInfo() {
        return new GroundSpawn(1);
    }
}