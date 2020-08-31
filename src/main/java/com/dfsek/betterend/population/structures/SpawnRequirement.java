package com.dfsek.betterend.population.structures;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public enum SpawnRequirement implements SpawnChecker {
    GROUND {
        public boolean isValidSpawn(NMSStructure s) {
            Location[] bounds = s.getBoundingLocations();
            return (!bounds[0].getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[0].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable());
        }

        @Override
        public int getHeight(int x, int z, Chunk c, Random r, Structure s) {
            return SpawnRequirement.getHighestBlock(c, x, z);
        }
    }, 
    GROUND_STRICT {
        public boolean isValidSpawn(NMSStructure s) {
            Location[] bounds = s.getBoundingLocations();
            return (!bounds[0].getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[0].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable());
        }
        @Override
        public int getHeight(int x, int z, Chunk c, Random r, Structure s) {
            return 0;
        }
    }, 
    SKY {
        public boolean isValidSpawn(NMSStructure s) {
            return true;
        }
        @Override
        public int getHeight(int x, int z, Chunk c, Random r, Structure s) {
            return 0;
        }
    }, 
    SKY_RANGE {
        public boolean isValidSpawn(NMSStructure s) {
            return true;
        }
        @Override
        public int getHeight(int x, int z, Chunk c, Random r, Structure s) {
            return 0;
        }
    },
    UNDERGROUND {
        public boolean isValidSpawn(NMSStructure s) {
            Location[] bounds = s.getBoundingLocations();
            return (!bounds[0].getBlock().isPassable())
                    && (!bounds[1].getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[0].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[1].getY(), bounds[0].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[1].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[1].getY(), bounds[1].getZ()).getBlock().isPassable());
        }
        @Override
        public int getHeight(int x, int z, Chunk c, Random r, Structure s) {
            return 0;
        }
    };
    
    public static int getHighestBlock(Chunk c, int x, int z) {
        int y;
        for(y = c.getWorld().getMaxHeight() - 1; (c.getBlock(x, y, z).getType() != Material.GRASS_BLOCK
                && c.getBlock(x, y, z).getType() != Material.GRAVEL
                && c.getBlock(x, y, z).getType() != Material.PODZOL
                && c.getBlock(x, y, z).getType() != Material.END_STONE
                && c.getBlock(x, y, z).getType() != Material.DIRT
                && c.getBlock(x, y, z).getType() != Material.STONE
                && c.getBlock(x, y, z).getType() != Material.COARSE_DIRT) && y > 0; y--);
        return y;
    }
    
    public boolean isSky() {
        return this.equals(SKY) || this.equals(SKY_RANGE);
    }
}
