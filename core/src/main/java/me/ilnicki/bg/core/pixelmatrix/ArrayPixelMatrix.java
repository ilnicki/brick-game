package me.ilnicki.bg.core.pixelmatrix;

import java.io.Serializable;

public class ArrayPixelMatrix implements Serializable, PixelMatrix {
    private final Pixel[][] pixelMatrix;
    private final int width;
    private final int height;

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
            this.width = width;
            this.height = height;
            pixelMatrix = new Pixel[height][width];
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
    public Pixel getPixel(int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return pixelMatrix[y][x];
        }

        return Pixel.WHITE;
    }

    @Override
    public void setPixel(int x, int y, Pixel value) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            pixelMatrix[y][x] = value;
        }
    }

    @Override
    public Pixel getPixel(Point point) {
        return pixelMatrix[point.getY()][point.getX()];
    }

    @Override
    public void setPixel(Point point, Pixel value) {
        setPixel(point.getY(), point.getX(), value);
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
