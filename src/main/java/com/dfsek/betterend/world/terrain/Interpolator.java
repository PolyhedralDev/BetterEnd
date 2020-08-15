package com.dfsek.betterend.world.terrain;

import com.dfsek.betterend.world.Biome;
import org.jetbrains.annotations.NotNull;

public class Interpolator {
    private final int[] _00Heights;
    private final int[] _10Heights;
    private final int[] _01Heights;
    private final int[] _11Heights;


    public Interpolator(@NotNull int[] _00, long seed) {

        this._00Heights = new int[] {Biome.fromCoordinates(_00[0], _00[1], seed).getGenerator().getMaxHeight(_00[0], _00[1], seed),
                Biome.fromCoordinates(_00[0], _00[1], seed).getGenerator().getMinHeight(_00[0], _00[1], seed)};
        this._01Heights = new int[] {Biome.fromCoordinates(_00[0], _00[1]+4, seed).getGenerator().getMaxHeight(_00[0], _00[1]+4, seed),
                Biome.fromCoordinates(_00[0], _00[1]+4, seed).getGenerator().getMinHeight(_00[0], _00[1]+4, seed)};
        this._10Heights = new int[] {Biome.fromCoordinates(_00[0]+4, _00[1], seed).getGenerator().getMaxHeight(_00[0]+4, _00[1], seed),
                Biome.fromCoordinates(_00[0]+4, _00[1], seed).getGenerator().getMinHeight(_00[0]+4, _00[1], seed)};
        this._11Heights = new int[] {Biome.fromCoordinates(_00[0]+4, _00[1]+4, seed).getGenerator().getMaxHeight(_00[0]+4, _00[1]+4, seed),
                Biome.fromCoordinates(_00[0]+4, _00[1]+4, seed).getGenerator().getMinHeight(_00[0]+4, _00[1]+4, seed)};
    }

    public double getMaxHeightAt(byte x, byte z) {
        if(x > 4 || z > 4) throw new IllegalArgumentException("Out of bounds!");
        double finalX = (float) x/4;
        double finalZ = (float) z/4;
        return blerp(_00Heights[0], _10Heights[0], _01Heights[0], _11Heights[0], finalX, finalZ);
    }
    public double getMinHeightAt(byte x, byte z) {
        if(x > 4 || z > 4) throw new IllegalArgumentException("Out of bounds!");
        double finalX = (float) x/4;
        double finalZ = (float) z/4;
        return blerp(_00Heights[1], _10Heights[1], _01Heights[1], _11Heights[1], finalX, finalZ);
    }

    private static double lerp(double s, double e, double t) {
        return s + (e - s) * t;
    }

    private static double blerp(double c00, double c10, double c01, double c11, double tx, double ty) {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
    }
}
