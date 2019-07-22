package me.ilnicki.bg.core.pixelmatrix;

import java.io.Serializable;

public class ArrayPixelMatrix implements Serializable, EditablePixelMatrix {
    private final Pixel[][] data;
    private final int width;
    private final int height;

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
            this.width = width;
            this.height = height;
            data = new Pixel[height][width];
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
        if(point.getX() >= 0 && point.getX() < width && point.getY() >= 0 && point.getY() < height) {
            return data[point.getY()][point.getX()];
        }

        return null;
    }

    @Override
    public void setPixel(Vector point, Pixel value) {
        if(point.getX() >= 0 && point.getX() < width && point.getY() >= 0 && point.getY() < height) {
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
