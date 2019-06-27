package me.ilnicki.bg.core.pixelmatrix;

public enum Pixel {
    BLACK,
    WHITE;

    public static Pixel merge(Pixel upper, Pixel lower) {
        if (upper == null) {
            return lower;
        }

        return upper;
    }
}
