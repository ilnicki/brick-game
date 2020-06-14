package me.ilnicki.bg.core.pixelmatrix;

import java.io.Serializable;
import java.util.HashMap;

public class HashPixelMatrix implements Serializable, MutablePixelMatrix {
    private final HashMap<Vector, Pixel> data;
    private final int width;
    private final int height;

    public HashPixelMatrix(PixelMatrix pm) {
        this(pm.getWidth(), pm.getHeight());

        for (int y = 0; y < pm.getHeight(); y++) {
            for (int x = 0; x < pm.getWidth(); x++) {
                Vector point = new Vector(x, y);
                setPixel(point, pm.getPixel(point));
            }
        }
    }

    public HashPixelMatrix(int width, int height) {
        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;
            data = new HashMap<>();
        } else {
            throw new IllegalArgumentException("Invalid boundary.");
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

    @Override
    public Pixel getPixel(Vector point) {
        return data.get(point);
    }

    @Override
    public void setPixel(Vector point, Pixel value) {
        if (value != null) {
            data.put(point, value);
        } else {
            data.remove(point);
        }
    }
}
