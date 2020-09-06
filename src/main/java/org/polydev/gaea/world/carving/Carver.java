package org.polydev.gaea.world.carving;

import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.MathUtil;

import java.util.Random;

public abstract class Carver {

    public CarvingData carve(int chunkX, int chunkZ, World w) {
        CarvingData data = new CarvingData(chunkX, chunkZ);
        if(isChunkCarved(new Random(MathUtil.hashToLong(this.getClass().getName() + "_" + chunkX + "&" + chunkZ)))) {
            for(int x = chunkX - 5; x < chunkX + 5; x++) {
                for(int z = chunkZ - 5; z < chunkZ + 5; z++) {
                    long seed = MathUtil.getCarverChunkSeed(x, z, w.getSeed());
                    Worm carving = getWorm(seed, new Vector((x << 4) + 8, new Random(seed).nextInt(150), (z << 4) + 8));
                    for(int i = 0; i < carving.getLength(); i++) {
                        carving.step();
                        carving.getPoint().carve(data, chunkX, chunkZ);
                    }
                }
            }
        }
        return data;
    }
    public abstract Worm getWorm(long seed, Vector l);
    public abstract boolean isChunkCarved(Random r);
}
