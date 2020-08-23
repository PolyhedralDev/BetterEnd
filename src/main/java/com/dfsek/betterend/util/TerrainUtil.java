package com.dfsek.betterend.util;

import com.dfsek.betterend.world.WorldConfig;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class TerrainUtil {
    public static double getVoidLevel(int x, int z, World w) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(w.getSeed(), 4);
		double d = biomeGenerator.noise((double) (x) / WorldConfig.fromWorld(w).biomeSize, (double) (z) / WorldConfig.fromWorld(w).biomeSize, 0.5D, 0.5D);
		if(d < 0 || d > 0.5) return 0;
		return -8D*Math.abs(d-0.25)+2D;
	}
	public static double getShatteredLevel(int x, int z, World w) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(w.getSeed(), 4);
		double d = biomeGenerator.noise((double) (x) / WorldConfig.fromWorld(w).biomeSize, (double) (z) / WorldConfig.fromWorld(w).biomeSize, 0.5D, 0.5D);
		if(d > -0.5) return 0;
		return (-3D*d -1.5 > 1) ? 1 : -3D*d -1.5;
	}
	public static boolean isAetherVoid(int x, int z, World w) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(w.getSeed(), 4);
		double d = biomeGenerator.noise((double) (x) / WorldConfig.fromWorld(w).biomeSize, (double) (z) / WorldConfig.fromWorld(w).biomeSize, 0.5D, 0.5D);
		return d > 0.4;
	}
}