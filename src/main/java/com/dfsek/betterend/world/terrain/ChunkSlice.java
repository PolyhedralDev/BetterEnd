package com.dfsek.betterend.world.terrain;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.Map;

public class ChunkSlice {
    private final Map<Integer, BlockData> slice = new HashMap<>();

    public ChunkSlice() {

    }

    public void setBlock(int y, BlockData data) {
        slice.put(y, data);
    }

    public void setBlock(int y, Material data) {
        slice.put(y, data.createBlockData());
    }

    public ChunkGenerator.ChunkData insert(ChunkGenerator.ChunkData data) {
        for(Map.Entry<Integer, BlockData> entry : slice.entrySet()) {
            data.setBlock(x, entry.getKey(), z, entry.getValue());
        }
        return data;
    }
}
