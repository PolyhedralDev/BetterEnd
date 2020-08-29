package com.dfsek.betterend.population.structures;

import org.bukkit.Location;

public enum SpawnRequirement implements SpawnChecker {
    GROUND {
        public boolean isValidSpawn(NMSStructure s) {
            Location[] bounds = s.getBoundingLocations();
            return (!bounds[0].getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[0].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                    && (!new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable());
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
    }, 
    SKY {
        public boolean isValidSpawn(NMSStructure s) {
            return true;
        }
    }, 
    SKY_RANGE {
        public boolean isValidSpawn(NMSStructure s) {
            return true;
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
    };

    public boolean isSky() {
        return this.equals(SKY) || this.equals(SKY_RANGE);
    }
}
