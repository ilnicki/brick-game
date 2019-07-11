package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public final class Screen implements PixelMatrix {
    private final int width;
    private final int height;
    private PixelMatrix data;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Screen(Field data) {
        this(data.getWidth(), data.getHeight());
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
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
