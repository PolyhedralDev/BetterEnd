package com.dfsek.betterend.world.generation.terrain;

import com.dfsek.betterend.world.Biome;
import com.dfsek.betterend.world.generation.terrain.biomes.EndGenerator;
import org.bukkit.Location;

public class Interpolator {
    
    private static EndGenerator end = new EndGenerator();
    private Location l1;
    private Location l2;

    /**
     *
     * @param l1 - The top-left location
     * @param l2 - The top-right location

     *
     * l1 * * *
     *  * * * *
     *  * * * *
     *  * * * l2
     *
     */
    public Interpolator(Location l1, Location l2) {
        if(!l1.getWorld().equals(l2.getWorld())) throw new IllegalArgumentException("Locations must be in the same world!");

        this.l1 = new Location(l1.getWorld(), Math.max(l1.getBlockX(), l2.getBlockX()), 0, Math.max(l1.getBlockZ(), l2.getBlockZ()));
        this.l2 = new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()), 0, Math.min(l1.getBlockZ(), l2.getBlockZ()));

        if((Math.abs(l1.getBlockX() - l2.getBlockX()) != 4) && (Math.abs(l1.getBlockZ() - l2.getBlockZ()) != 4)) {
            throw new IllegalArgumentException("Not 4 blocks apart!");
        }
    }

    public int getHeightAt(byte x, byte z) {
        if(x > 4 || z > 4) throw new IllegalArgumentException("Out of bounds!");
        Location l = l1.clone().subtract(x, 0, z);
        return Biome.fromLocation(l).getGenerator().getHeight(l);
    }
}
