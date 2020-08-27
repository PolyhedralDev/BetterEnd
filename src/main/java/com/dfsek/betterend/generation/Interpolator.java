package com.dfsek.betterend.generation;

public class Interpolator {
    private final double v0, v1, v2, v3;
    public Interpolator(double v0, double v1, double v2, double v3) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    public Interpolator() {
        this.v0 = 0;
        this.v1 = 0;
        this.v2 = 0;
        this.v3 = 0;
    }
    public double lerp(double t,double  v0, double v1) {
        return v0 + t*(v1-v0);
    }
    public double bilerp(double s, double t) {
        double v01 = lerp(s, v0, v1);
        double v23 = lerp(s, v2, v3);
        double v = lerp(t, v01, v23);
        return v;
    }
}