package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public final class Screen implements PixelMatrix {
    private PixelMatrix data;

    Screen(Field data) {
        setData(data);
    }

    void setData(Field data) {
        this.data = data;
    }

    @Override
    public Pixel getPixel(Vector point) {
        if (point.getX() >= getWidth() || point.getX() < 0
                || point.getY() >= getHeight() || point.getY() < 0) {
            return Pixel.WHITE;
        } else {
            return Pixel.merge(data.getPixel(point), Pixel.WHITE);
        }
    }

    @Override
    public int getWidth() {
        return data.getWidth();
    }

    @Override
    public int getHeight() {
        return data.getHeight();
    }
}
