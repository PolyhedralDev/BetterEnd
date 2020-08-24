package com.dfsek.betterend.world.population.tree;

import com.dfsek.betterend.util.ConfigUtil;
import com.dfsek.betterend.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Random;

public class ShatteredTree extends Tree {
    private final boolean large;

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     * @param large  - Whether the tree is large.
     */
    public ShatteredTree(Location origin, Random random, boolean large) {
        super(origin, random);
        this.large = large;
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Growing tree...");
        Random random = super.getRandom();
        Location start = super.getOrigin();
        double startR = 1;
        int length = random.nextInt(5) + 5;
        if(large) {
            startR = 2;
            length = random.nextInt(10) + 20;
        }
        Vector initV = new Vector(0, 1, 0);
        int roots = random.nextInt(3) + 3;
        for(int j = 0; j < roots; j++)
            doRootAt(start.clone(), startR, random, random.nextInt(10) + 10, initV.clone(), j * (360 / roots));
        for(int i = 0; i < length; i++) {
            start.add(initV);
            for(int x = (int) -startR; x <= startR; x++) {
                for(int y = (int) -startR; y <= startR; y++) {
                    for(int z = (int) -startR; z <= startR; z++) {
                        Vector position = start.toVector().clone().add(new Vector(x, y, z));

                        if(start.toVector().distance(position) <= startR + 0.5 && position.toLocation(start.getWorld()).getBlock().isPassable() && getMaterial(position.toLocation(start.getWorld()).getBlock()).isAir()) {
                            setBlock(position.toLocation(start.getWorld()).getBlock(), (random.nextBoolean() ? Material.OBSIDIAN : Material.BLACK_CONCRETE));
                        }
                    }
                }
            }
            initV.add(new Vector(Util.getOffset(random, 0.1), Util.getOffset(random, 0.1), Util.getOffset(random, 0.1)));
            if(initV.getX() > 1) initV.setX(1);
            if(initV.getX() < -1) initV.setX(-1);
            if(initV.getY() > 1) initV.setY(1);
            if(initV.getY() < 0) initV.setY(0);
            if(initV.getZ() > 1) initV.setZ(1);
            if(initV.getZ() < -1) initV.setZ(-1);
            startR = startR - 0.05;
            int branches = random.nextInt(2) + 1;
            if(i % 3 == 0 && i > length / 3) for(int j = 0; j < branches; j++)
                doBranchAt(start.clone(), startR, random, (int) (random.nextInt(7) + 7 * startR), initV.clone());
        }
    }
    private void doBranchAt(Location start, double startR, Random random, int length, Vector startV) {
        if(length < 4) return;
        int ogStart = (int) startR;
        if(startR < 0) return;
        Vector initV = getPerpendicular(startV.clone()).rotateAroundAxis(startV, random.nextInt(360));

        for(int i = 0; i < length; i++) {
            doSphereAtLoc(start.add(initV), (int) startR, random);
            startR = startR - 0.125;
            initV.add(new Vector(Util.getOffset(random, 0.1), Util.getOffset(random, 0.1), Util.getOffset(random, 0.1)));
            if(initV.getX() > 1) initV.setX(1);
            if(initV.getX() < -1) initV.setX(-1);
            if(initV.getY() > 1) initV.setY(1);
            if(initV.getY() < -1) initV.setY(-1);
            if(initV.getZ() > 1) initV.setZ(1);
            if(initV.getZ() < -1) initV.setZ(-1);
            int branches = random.nextInt(2) + 1;
            if(i % 4 == 0 && i > length / 4) for(int j = 0; j < branches; j++)
                doBranchAt(start.clone(), startR, random, random.nextInt((length / 3) + 1) + length / 3, initV.clone());
        }
        int radius = random.nextInt(ogStart + 1) + 1;
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Vector position = start.toVector().clone().add(new Vector(x, y, z));
                    if(start.toVector().distance(position) <= radius + 0.5 && position.toLocation(start.getWorld()).getBlock().isPassable() && getMaterial(position.toLocation(start.getWorld()).getBlock()).isAir()) {
                        setBlock(position.toLocation(start.getWorld()).getBlock(), (random.nextBoolean() ? Material.MAGENTA_STAINED_GLASS : Material.PURPLE_STAINED_GLASS));
                    }
                }
            }
        }
    }

    private void doRootAt(Location start, double startR, Random random, int length, Vector startV, int angle) {
        if(length < 4) return;
        if(startR < 0) return;
        Vector initV = getPerpendicular(startV.clone()).rotateAroundAxis(startV, angle).setY(-0.2);
        for(int i = 0; i < length; i++) {
            start.add(initV);
            boolean empty = start.getBlock().isEmpty();
            int radius = (int) startR;
            for(int x = -radius; x <= radius; x++) {
                for(int y = -radius; y <= radius; y++) {
                    for(int z = -radius; z <= radius; z++) {
                        Vector position = start.toVector().clone().add(new Vector(x, y, z));
                        if(start.toVector().distance(position) <= radius + 0.5 && position.toLocation(start.getWorld()).getBlock().getType() == Material.END_STONE
                                || position.toLocation(start.getWorld()).getBlock()
                                .isEmpty()) setBlock(position.toLocation(start.getWorld()).getBlock(), (random.nextBoolean() ? Material.OBSIDIAN : Material.BLACK_CONCRETE));
                    }
                }
            }
            startR = startR - 0.175;
            if(empty) {
                startR = startR - 0.1;
                initV = new Vector(initV.getX() / 4, -1, initV.getZ() / 4);
            } else {
                initV.add(new Vector(Util.getOffset(random, 0.25), Util.getOffset(random, 0.25), Util.getOffset(random, 0.25)));
            }
            if(initV.getX() > 1) initV.setX(1);
            if(initV.getX() < -1) initV.setX(-1);
            if(initV.getY() > 0.1) initV.setY(0.1);
            if(initV.getY() < -1) initV.setY(-1);
            if(initV.getZ() > 1) initV.setZ(1);
            if(initV.getZ() < -1) initV.setZ(-1);
        }
    }

    private void doSphereAtLoc(Location l, int radius, Random random) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, y, z));
                    if(l.toVector().distance(position) <= radius + 0.5 && position.toLocation(l.getWorld()).getBlock().isEmpty() && getMaterial(position.toLocation(l.getWorld()).getBlock()).isAir())
                        setBlock(position.toLocation(l.getWorld()).getBlock(), (random.nextBoolean() ? Material.OBSIDIAN : Material.BLACK_CONCRETE));
                }
            }
        }
    }
}
