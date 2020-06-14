package me.ilnicki.bg.core.pixelmatrix;

import java.io.Serializable;

public class ArrayPixelMatrix implements Serializable, MutablePixelMatrix {
    private final Pixel[][] data;
    private final Rectangle boundary;

    public ArrayPixelMatrix(PixelMatrix pm) {
        this(pm.getWidth(), pm.getHeight());

        for (int y = 0; y < pm.getHeight(); y++) {
            for (int x = 0; x < pm.getWidth(); x++) {
                Vector point = new Vector(x, y);
                setPixel(point, pm.getPixel(point));
            }
        }
    }

    public ArrayPixelMatrix(int width, int height) {
        if (width >= 0 && height >= 0) {
            boundary = new Rectangle(width, height);
            data = new Pixel[height][width];
        } else {
            throw new IllegalArgumentException("Invalid boundary.");
        }
    }

    @Override
    public int getWidth() {
        return boundary.getWidth();
    }

    @Override
    public int getHeight() {
        return boundary.getHeight();
    }

    @Override
    public Pixel getPixel(Vector point) {
        if (boundary.contains(point)) {
            return data[point.getY()][point.getX()];
        }

        return null;
    }

    @Override
    public void setPixel(Vector point, Pixel value) {
        if (boundary.contains(point)) {
            data[point.getY()][point.getX()] = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getClass().getSimpleName())
                .append(": width = ").append(getWidth())
                .append("; height = ").append(getHeight())
                .append(";\n");

        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < getWidth(); x++) {
                final Vector point = new Vector(x, y);

                if (getPixel(point) != null) {
                    sb.append(getPixel(point));
                } else {
                    sb.append("   ");
                }
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
