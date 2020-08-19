package com.dfsek.betterend.world.tree;

import com.dfsek.betterend.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class WoodTree extends Tree {
    private CustomTreeType type;

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public WoodTree(Location origin, Random random, CustomTreeType type) {
        super(origin, random);
        this.type = type;
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        Vector initV = new Vector(0, 1, 0);
        Random random = super.getRandom();
        Location start = super.getOrigin();
        double startR = 1.5;
        double length = random.nextInt(4) + 10;
        int roots = random.nextInt(3) + 3;
        switch(type) {
            case OAK:
                for(int j = 0; j < roots; j++)
                    doWoodRootAt(start.clone(), startR, random, random.nextInt(10) + 10, initV.clone(), j * (360 / roots), Material.OAK_WOOD);
                for(int i = 0; i < length; i++) {
                    start.add(initV);
                    for(int x = (int) -startR; x <= startR; x++) {
                        for(int y = (int) -startR; y <= startR; y++) {
                            for(int z = (int) -startR; z <= startR; z++) {
                                Vector position = start.toVector().clone().add(new Vector(x, y, z));
                                if(start.toVector().distance(position) <= startR + 0.5 && position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock().isPassable()  && getMaterial(position.toLocation(start.getWorld()).getBlock()).isAir()) {
                                    setBlock(position.toLocation(start.getWorld()).getBlock(), Material.OAK_WOOD);
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
                    int branches = random.nextInt(2) + 2;
                    if(i % 3 == 0 && i > length / 3) for(int j = 0; j < branches; j++)
                        doWoodBranchAt(start.clone(), startR, random, (int) (random.nextInt(7) + 7 * startR), initV.clone(), Material.OAK_WOOD, Material.OAK_LEAVES, 0,
                                false);
                }
                break;
            case SPRUCE:
                length = 3 * (random.nextInt(3) + 5);
                int ogStart = (int) startR;
                for(int j = 0; j < roots; j++)
                    doWoodRootAt(start.clone(), startR, random, random.nextInt(10) + 10, initV.clone(), j * (360 / roots), Material.SPRUCE_WOOD);
                for(int i = 0; i < length; i++) {
                    start.add(initV);
                    for(int x = (int) -startR; x <= startR; x++) {
                        for(int y = (int) -startR; y <= startR; y++) {
                            for(int z = (int) -startR; z <= startR; z++) {
                                Vector position = start.toVector().clone().add(new Vector(x, y, z));
                                if(start.toVector().distance(position) <= startR + 0.5 && position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock().isPassable() && getMaterial(position.toLocation(start.getWorld()).getBlock()).isAir()) {
                                    setBlock(position.toLocation(start.getWorld()).getBlock(), Material.SPRUCE_WOOD);
                                }
                            }
                        }
                    }
                    int r = (int) (((3 - (i % 3)) + 2) * (startR / ogStart));
                    if(i > (length / 6) - 1) {
                        for(int x = -r; x <= r; x++) {
                            for(int z = -r; z <= r; z++) {
                                Vector position = start.toVector().clone().add(new Vector(x, 0, z));
                                if(start.toVector().distance(position) <= r + 0.5 && position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock().isPassable() && getMaterial(position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock()).isAir()) {
                                    setBlock(position.toLocation(start.getWorld()).getBlock(), Material.SPRUCE_LEAVES);
                                }
                            }
                        }
                    }
                    startR = startR - 0.05;
                }
                start.add(initV);
                setBlock(start.getBlock(), Material.SPRUCE_LEAVES);
                start.add(initV);
                setBlock(start.getBlock(), Material.SPRUCE_LEAVES);
                break;
            default:
                throw new IllegalArgumentException("Invalid tree type specified: " + type);
        }
    }
    private void doWoodBranchAt(Location start, double startR, Random random, int length, Vector startV, Material m, Material l, int lvl, boolean doYCheck) {
        if(length < 4) return;
        if(startR < 0) return;
        Vector initV = getPerpendicular(startV.clone()).rotateAroundAxis(startV, random.nextInt(360));
        if (initV.getY() < 0 && doYCheck) initV = initV.rotateAroundAxis(startV, 180);
        for(int i = 0; i < length; i++) {
            doMSphereAtLoc(start.add(initV), (int) startR, m);
            if(lvl >= 1) {
                for(int x = -5; x <= 5; x++) {
                    for(int y = -5; y <= 5; y++) {
                        for(int z = -5; z <= 5; z++) {
                            if(start.clone().add(x, y, z).getBlock().isPassable() && start.clone().add(x, y, z).distance(start) < 2.5
                                    && start.clone().add(x, y, z).getBlock().isEmpty() && getMaterial(start.clone().add(x, y, z).getBlock()).isAir()) {
                                setBlock(start.clone().add(x, y, z).getBlock(), l);
                            }
                        }
                    }
                }
            }
            startR = startR - 0.1;
            initV.add(new Vector(Util.getOffset(random, 0.1), Util.getOffset(random, 0.1), Util.getOffset(random, 0.1)));
            if(initV.getX() > 1) initV.setX(1);
            if(initV.getX() < -1) initV.setX(-1);
            if(initV.getY() > 1) initV.setY(1);
            if(initV.getY() < -1) initV.setY(-1);
            if(initV.getZ() > 1) initV.setZ(1);
            if(initV.getZ() < -1) initV.setZ(-1);
            int branches = random.nextInt(3) + 1;
            if(i % 3 == 0 && i > length / 4) for(int j = 0; j < branches; j++)
                doWoodBranchAt(start.clone(), startR, random, random.nextInt((length / 2) + 1) + length / 3, initV.clone(), m, l, lvl++, true);
        }
    }

    private void doWoodRootAt(Location start, double startR, Random random, int length, Vector startV, int angle, Material m) {
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
                        if(start.toVector().distance(position) <= radius + 0.5 && position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock().getType() == Material.END_STONE
                                || position.toLocation(Objects.requireNonNull(start.getWorld())).getBlock().isEmpty() || position.toLocation(start.getWorld()).getBlock().getType() == Material.GRASS
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.TALL_GRASS
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.GRASS_BLOCK
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.DIRT
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.STONE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.COBBLESTONE_WALL
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.IRON_BARS
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.STONE_BUTTON
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.RED_MUSHROOM
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.BROWN_MUSHROOM
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.COAL_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.IRON_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.GOLD_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.REDSTONE_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.EMERALD_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.LAPIS_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.DIAMOND_ORE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.LANTERN
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.STONE_SLAB
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.SAND
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.WATER
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.ANDESITE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.DIORITE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.GRANITE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.COBBLESTONE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.END_STONE
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.END_STONE_BRICK_WALL
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.END_STONE_BRICK_SLAB
                                || position.toLocation(start.getWorld()).getBlock().getType() == Material.END_ROD) {
                            setBlock(position.toLocation(start.getWorld()).getBlock(), m);
                        }
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

    private void doMSphereAtLoc(Location l, int radius, Material m) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, y, z));

                    if(l.toVector().distance(position) <= radius + 0.5 && (position.toLocation(Objects.requireNonNull(l.getWorld())).getBlock().isEmpty()
                            || position.toLocation(l.getWorld()).getBlock().getType() == Material.OAK_LEAVES)) {
                        setBlock(position.toLocation(l.getWorld()).getBlock(), m);
                    }
                }
            }
        }
    }
}
