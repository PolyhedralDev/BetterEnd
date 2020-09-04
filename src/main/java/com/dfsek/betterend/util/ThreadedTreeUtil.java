package com.dfsek.betterend.util;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.Location;
import org.polydev.gaea.tree.CustomTreeType;
import org.polydev.gaea.tree.fractal.FractalTree;
import org.polydev.gaea.tree.fractal.trees.OakTree;
import org.polydev.gaea.tree.fractal.trees.SpruceTree;

import java.util.Random;

public class ThreadedTreeUtil {
    private static final BetterEnd main = BetterEnd.getInstance();

    public static void plantLargeTree(CustomTreeType type, Location origin, Random random) {
        FractalTree tree;
        switch(type) {
            case GIANT_OAK:
                tree = new OakTree(origin, random);
                break;
            case GIANT_SPRUCE:
                tree = new SpruceTree(origin, random);
                break;
            default:
                throw new IllegalArgumentException();
        }
        FractalTree finalTree = tree;
        main.getFactory().newChain()
                .async(tree::grow)
                .sync(() -> finalTree.plant(true))
                .execute();
    }
}
