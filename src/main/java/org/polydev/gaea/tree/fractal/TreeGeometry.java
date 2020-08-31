package org.polydev.gaea.tree.fractal;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class TreeGeometry {
    private final FractalTree tree;
    public TreeGeometry(FractalTree tree) {
        this.tree = tree;
    }
    public FractalTree getTree() {
        return tree;
    }
    public static Vector getPerpendicular(Vector v) {
        return v.getZ() < v.getX() ? new Vector(v.getY(), -v.getX(), 0) : new Vector(0, -v.getZ(), v.getY());
    }

    public void generateSphere(Location l, Material m, int radius, boolean overwrite) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, y, z));
                    if(l.toVector().distance(position) <= radius + 0.5 && (overwrite || tree.getMaterial(position.toLocation(l.getWorld()).getBlock()).isAir()))
                        tree.setBlock(position.toLocation(l.getWorld()).getBlock(), m);
                }
            }
        }
    }

    public void generateCylinder(Location l, Material m, int radius, int height, boolean overwrite) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = 0; y <= height; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Vector position = l.toVector().clone().add(new Vector(x, 0, z));
                    if(l.toVector().distance(position) <= radius + 0.5 && (overwrite || tree.getMaterial(position.toLocation(l.getWorld()).getBlock()).isAir()))
                        tree.setBlock(position.toLocation(l.getWorld()).getBlock(), m);
                }
            }
        }
    }
}