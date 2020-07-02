package me.ilnicki.bg.core.pixelmatrix.layering;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class LayerList implements PixelMatrix {
    private final int width;
    private final int height;
    private final List<Layer<PixelMatrix>> layers;

    public LayerList(int width, int height) {
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
    public Pixel getPixel(Vector point) {
        if (point.getX() >= getWidth() || point.getX() < 0 || point.getY() >= getHeight() || point.getY() < 0) {
            return null;
        }

        return layers.stream().map(layer -> layer.getPixel(point)).reduce(null, Pixel::merge);
    }

    public List<Layer<PixelMatrix>> getLayers() {
        return layers;
    }
}
