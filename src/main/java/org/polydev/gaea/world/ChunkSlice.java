package org.polydev.gaea.world;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.Map;

public class ChunkSlice {
    private final Map<Integer, BlockData> slice = new HashMap<>();
    private final byte x;
    private final byte z;

    public ChunkSlice(byte x, byte z) {
        this.x = x;
        this.z = z;
    }

    public void setBlock(int y, BlockData data) {
        slice.put(y, data);
    }

    public void setBlock(int y, Material data) {
        slice.put(y, data.createBlockData());
    }

    public ChunkGenerator.ChunkData insert(ChunkGenerator.ChunkData data) {
        for(Map.Entry<Integer, BlockData> entry : slice.entrySet()) {
            data.setBlock(this.x, entry.getKey(), this.z, entry.getValue());
        }
        return data;
    }

    public Map<Integer, BlockData> getAsMap() {
        return slice;
    }

    public void merge(ChunkSlice c) {
        for(Map.Entry<Integer, BlockData> entry : c.getAsMap().entrySet()) {
            this.slice.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }
}
