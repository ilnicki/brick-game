package me.ilnicki.bg.core.pixelmatrix.modifying;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;

public class Invert implements PixelMatrix {
    private final PixelMatrix target;
    private final PixelMatrix mask;

    public Invert(PixelMatrix target, PixelMatrix mask) {
        this.target = target;
        this.mask = mask;
    }

    @Override
    public int getWidth() {
        return target.getWidth();
    }

    @Override
    public int getHeight() {
        return target.getHeight();
    }

    @Override
    public Pixel getPixel(Vector point) {
        Pixel targetPixel = target.getPixel(point);

        if (targetPixel == null) {
            return null;
        }

        Pixel inverterPixel = mask.getPixel(point);

        if (inverterPixel == Pixel.BLACK) {
            return Pixel.invert(targetPixel);
        }

        return targetPixel;
    }
}
