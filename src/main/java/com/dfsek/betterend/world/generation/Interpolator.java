package com.dfsek.betterend.world.generation;

public class Interpolator {
    public static double lerp(double t,double  v0, double v1) {
        return v0 + t*(v1-v0);
    }
    public static double bilerp(double s, double t, double v0, double v1, double v2, double v3) {
        double v01 = lerp(s, v0, v1);
        double v23 = lerp(s, v2, v3);
        double v = lerp(t, v01, v23);
        return v;
    }
}