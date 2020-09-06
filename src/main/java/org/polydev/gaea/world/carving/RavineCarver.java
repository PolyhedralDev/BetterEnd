package org.polydev.gaea.world.carving;


import org.bukkit.util.Vector;

import java.util.Random;

public class RavineCarver extends Carver {
    @Override
    public Worm getWorm(long seed, Vector l) {
        return new RavineWorm(new Random(seed+1).nextInt(20) + 60, new Random(seed), l);
    }

    @Override
    public boolean isChunkCarved(Random r) {
        return r.nextInt(100) < 10;
    }

    public static class RavineWorm extends Worm {
        private final Vector direction;
        public RavineWorm(int length, Random r, Vector origin) {
            super(length, r, origin);
            direction = new Vector(r.nextDouble()-0.5D, (r.nextDouble()-0.5D)/8, r.nextDouble()-0.5D).normalize();
        }

        @Override
        public void step() {
            setRadius(new int[] {3, 8, 3});
            direction.rotateAroundX(Math.toRadians(getRandom().nextDouble()*4));
            direction.rotateAroundY(Math.toRadians(getRandom().nextDouble()*12));
            direction.rotateAroundZ(Math.toRadians(getRandom().nextDouble()*4));
            getRunning().add(direction);
        }
    }
}
