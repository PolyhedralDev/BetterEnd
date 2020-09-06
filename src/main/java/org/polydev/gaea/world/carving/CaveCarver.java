package org.polydev.gaea.world.carving;


import org.bukkit.util.Vector;

import java.util.Random;

public class CaveCarver extends Carver {
    @Override
    public Worm getWorm(long seed, Vector l) {
        return new CaveWorm(new Random(seed).nextInt(45) + 30, new Random(seed), l);
    }

    @Override
    public boolean isChunkCarved(Random r) {
        return true;
    }

    public static class CaveWorm extends Worm {
        private final Vector direction;
        public CaveWorm(int length, Random r, Vector origin) {
            super(length, r, origin);
            direction = new Vector(r.nextDouble()-0.5D, (r.nextDouble()-0.5D)/4, r.nextDouble()-0.5D).normalize();
        }

        @Override
        public void step() {
            setRadius(new int[] {2, 2, 2});
            direction.rotateAroundX(Math.toRadians(getRandom().nextDouble()*4));
            direction.rotateAroundY(Math.toRadians(getRandom().nextDouble()*12));
            direction.rotateAroundZ(Math.toRadians(getRandom().nextDouble()*4));
            getRunning().add(direction);
        }
    }
}
