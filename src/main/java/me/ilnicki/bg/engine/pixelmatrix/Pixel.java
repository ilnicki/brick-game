package me.ilnicki.bg.engine.pixelmatrix;

public enum Pixel {
    BLACK,
    WHITE;

    public static Pixel merge(Pixel upperPixel, Pixel lowerPixel) {
        if (upperPixel == null) {
            return lowerPixel;
        } else {
            return upperPixel;
        }
    }
}
