package com.dfsek.betterend.util;

import org.bukkit.util.noise.SimplexOctaveGenerator;

public class TerrainUtil {
    public static double getVoidLevel(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		if(d < 0 || d > 0.5) return 0;
		return -8D*Math.abs(d-0.25)+2D;
	}
	public static double getShatteredLevel(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		if(d > -0.5) return 0;
		return (-3D*d -1.5 > 1) ? 1 : -3D*d -1.5;
	}
	public static boolean isAetherVoid(int x, int z, long seed) {
		SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(seed, 4);
		double d = biomeGenerator.noise((double) (x) / ConfigUtil.biomeSize, (double) (z) / ConfigUtil.biomeSize, 0.5D, 0.5D);
		return d > 0.4;
	}
}