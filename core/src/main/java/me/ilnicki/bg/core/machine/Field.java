package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

import java.util.ArrayList;
import java.util.List;

public final class Field implements PixelMatrix {
    private final int width;
    private final int height;
    private final List<Layer> layers;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        layers = new ArrayList<>();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Pixel getPixel(int x, int y) {
        if (x >= getWidth() || x < 0 || y >= getHeight() || y < 0) {
            return null;
        }

        Vector point = new Vector(x, y);
        return layers.stream().map(layer -> layer.getPixel(point)).reduce(null, Pixel::merge);
    }

    @Override
    public Pixel getPixel(Vector point) {
        return this.getPixel(point.getX(), point.getY());
    }

    @Override
    public void setPixel(Vector point, Pixel value) {
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
