package me.ilnicki.bg.engine.machine;

import me.ilnicki.bg.engine.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Point;

import java.util.ArrayList;
import java.util.List;

public final class Field implements PixelMatrix {
    private final PixelMatrix baseLayer;
    private final List<Layer> layers;

    public Field(int width, int height) {
        baseLayer = new ArrayPixelMatrix(width, height);
        layers = new ArrayList<>();
    }

    @Override
    public int getWidth() {
        return baseLayer.getWidth();
    }

    @Override
    public int getHeight() {
        return baseLayer.getHeight();
    }

    @Override
    public Pixel getPixel(int x, int y) {
        if (x >= this.getWidth() || x < 0 || y >= this.getHeight() || y < 0) {
            throw new IndexOutOfBoundsException(String.format("Wrong matrix element indexes [%d, %d].", x, y));
        } else {
            try {
                Pixel result = this.baseLayer.getPixel(x, y);

                for (Layer layer : layers) {
                    result = Pixel.merge(layer.getPixel(x, y), result);
                }

                return result;
            } catch (Exception e) {
                return null;
            }
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

    public PixelMatrix getBaseLayer() {
        return baseLayer;
    }


    public List<Layer> getLayers() {
        return layers;
    }
}
