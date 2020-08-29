package com.dfsek.betterend.population.tree.fractal.trees;

import com.dfsek.betterend.population.tree.fractal.FractalTree;
import org.bukkit.Location;

import java.util.Random;

public class Oak extends FractalTree {
    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public Oak(Location origin, Random random) {
        super(origin, random);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        growBranch();
    }
    private void growBranch() {

    }
}
