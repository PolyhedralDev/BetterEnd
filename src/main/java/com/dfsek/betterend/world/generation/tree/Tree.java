package com.dfsek.betterend.world.generation.tree;

import com.dfsek.betterend.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Tree {
    private final Map<Block, Material> treeAssembler = new HashMap<>();
    private final Location origin;
    private final Random random;
    /**
     * Instantiates a TreeGrower at an origin location.
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public Tree(Location origin, Random random) {
        this.origin = origin;
        this.random = random;
    }

    /**
     * Gets the raw tree map.
     * @return HashMap<Block, Material> - The raw dictionary representation of the tree.
     */
    public Map<Block, Material> getTree() {
        return treeAssembler;
    }


    /**
     * Fetches the Random object used to generate the tree.
     * @return Random - The Random object.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Fetches the origin location.
     * @return Location - The origin location specified upon instantiation.
     */
    public Location getOrigin() {
        return origin;
    }

    /**
     * Sets a block in the tree's storage map to a material.
     * @param b - The block to set.
     * @param m - The material to which it will be set.
     */
    public void setBlock(Block b, Material m) {
        treeAssembler.put(b, m);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    public abstract void grow();

    /**
     * Pastes the tree in the world. Must be invoked from main thread.
     */
    public void plant() {
        if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Planting tree...");
        for(Map.Entry<Block, Material> entry : treeAssembler.entrySet()) {
            entry.getKey().setType(entry.getValue());
        }
    }

    /**
     * Gets the material at the specified block.
     * Returns air if no material has been set.
     * @param b - The block at which to check.
     * @return Material - The material at the specified block.
     */
    public Material getMaterial(Block b) {
        return treeAssembler.getOrDefault(b, Material.AIR);
    }

    public static Vector getPerpendicular(Vector v) {
        return v.getZ() < v.getX() ? new Vector(v.getY(), -v.getX(), 0) : new Vector(0, -v.getZ(), v.getY());
    }
}
