package me.ilnicki.bg.engine.pixelmatrix;

import java.io.Serializable;

public class ArrayPixelMatrix implements Serializable, PixelMatrix {
    private final Pixel[][] pixelMatrix;

    public ArrayPixelMatrix(PixelMatrix pm) {
        this(pm.getWidth(), pm.getHeight());

        for (int y = 0; y < pm.getHeight(); y++) {
            for (int x = 0; x < pm.getWidth(); x++) {
                setPixel(x, y, pm.getPixel(x, y));
            }
        }
    }

    public ArrayPixelMatrix(int width, int height) {
        if (width > 0 && height > 0) {
            pixelMatrix = new Pixel[height][width];
        } else {
            throw new IllegalArgumentException("Invalid boundary.");
        }
    }

    @Override
    public int getWidth() {
        return pixelMatrix[0].length;
    }

    @Override
    public int getHeight() {
        return pixelMatrix.length;
    }

    @Override
    public Pixel getPixel(int x, int y) {
        return pixelMatrix[y][x];
    }

    @Override
    public void setPixel(int x, int y, Pixel value) {
        pixelMatrix[y][x] = value;
    }

    @Override
    public Pixel getPixel(Point point) {
        return pixelMatrix[point.getY()][point.getX()];
    }

    @Override
    public void setPixel(Point point, Pixel value) {
        pixelMatrix[point.getY()][point.getX()] = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getClass().getSimpleName())
                .append(": width = ").append(getWidth())
                .append("; height = ").append(getHeight())
                .append(";\n");

        for (int i = getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < getWidth(); j++) {
                if (getPixel(j, i) != null) {
                    sb.append('[')
                            .append(getPixel(j, i) == Pixel.BLACK ? 'X' : ' ')
                            .append(']');
                } else {
                    sb.append("   ");
                }
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
