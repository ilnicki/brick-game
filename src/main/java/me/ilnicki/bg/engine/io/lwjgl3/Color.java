package me.ilnicki.bg.engine.io.lwjgl3;

public class Color {
    private final byte red;
    private final byte green;
    private final byte blue;

    private final float factor = 1.0f / 255;

    public Color(int red, int green, int blue) {
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    public byte getR() {
        return red;
    }

    public byte getG() {
        return green;
    }

    public byte getB() {
        return blue;
    }

    public float getFloatR() {
        return red * factor;
    }

    public float getFloatG() {
        return green * factor;
    }

    public float getFloatB() {
        return blue * factor;
    }

    @Override
    public String toString() {
        return String.format("#%02x%02x%02x", this.red,
                this.red,
                this.blue).toUpperCase();
    }
}