package me.ilnicki.bg.engine.machine;

import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Point;

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
        if (x >= this.getWidth() || x < 0 || y >= this.getHeight() || y < 0) {
            return Pixel.WHITE;
        } else {
            Pixel result = Pixel.WHITE;

            for (Layer layer : layers) {
                result = Pixel.merge(layer.getPixel(x, y), result);
            }

            return result;
        }
    }

    @Override
    public void setPixel(int x, int y, Pixel value) {
    }

    @Override
    public Pixel getPixel(Point point) {
        return this.getPixel(point.getX(), point.getY());
    }

    @Override
    public void setPixel(Point point, Pixel value) {
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
