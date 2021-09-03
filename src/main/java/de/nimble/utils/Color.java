package de.nimble.utils;

public class Color {

    private float r;
    private float g;
    private float b;
    private float a;

    public Color() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
    }

    public Color(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getR() {
        return r;
    }

    public void setR(final float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(final float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(final float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(final float a) {
        this.a = a;
    }
}
