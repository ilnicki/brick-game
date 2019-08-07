package me.ilnicki.bg.core.pixelmatrix.modifying;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public class Invert implements PixelMatrix {
    private final PixelMatrix target;
    private final PixelMatrix inverter;

    public Invert(PixelMatrix target, PixelMatrix inverter) {
        this.target = target;
        this.inverter = inverter;
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

        if(targetPixel == null) {
            return null;
        }

        Pixel inverterPixel = inverter.getPixel(point);

        if(inverterPixel == Pixel.BLACK) {
            return Pixel.invert(targetPixel);
        }

        return targetPixel;
    }
}
