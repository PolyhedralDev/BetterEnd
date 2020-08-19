package com.dfsek.betterend.world.tree;

import com.dfsek.betterend.BetterEnd;
import com.dfsek.betterend.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;

public class ThreadedTreeUtil {
    private static final BetterEnd main = BetterEnd.getInstance();
    public static void plantLargeTree(CustomTreeType type, Location origin, Random random) {
        if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Generating async tree of type " + type.toString());
        long t = System.nanoTime();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if(ConfigUtil.debug) Bukkit.getLogger().info("[" + Thread.currentThread().getName() + "] Building async tree!");
            boolean large = true;
            switch(type) {
                case SHATTERED_SMALL:
                    large = false;
                case SHATTERED_LARGE:
                    ShatteredTree tree = new ShatteredTree(origin, random, large);
                    tree.grow();
                    if(ConfigUtil.debug) main.getLogger().info("[" + Thread.currentThread().getName() + "] Time saved: " + (System.nanoTime() - t)/1000000 + "ms");
                    Bukkit.getScheduler().runTask(main, () -> tree.plant(true));
                    break;
                case SPRUCE:
                case OAK:
                    WoodTree woodTree = new WoodTree(origin, random, type);
                    woodTree.grow();
                    if(ConfigUtil.debug) main.getLogger().info("[" + Thread.currentThread().getName() + "] Time saved: " + (System.nanoTime() - t)/1000000 + "ms");
                    Bukkit.getScheduler().runTask(main, () -> woodTree.plant(true));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid tree type.");
            }

        });
    }
}
