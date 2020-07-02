package me.ilnicki.bg.core.pixelmatrix;

import me.ilnicki.bg.core.math.Rectangle;
import me.ilnicki.bg.core.math.Vector;

public class ConstantPixelMatrix implements PixelMatrix {
    private final Pixel[][] data;
    private final Rectangle boundary;

    private ConstantPixelMatrix(Pixel[][] data, Rectangle boundary) {
        this.data = new Pixel[boundary.getHeight()][boundary.getWidth()];

        for(int i = 0; i < boundary.getHeight(); i++)
        {
            System.arraycopy(data[i], 0, this.data[i], 0, boundary.getWidth());
        }

        this.boundary = boundary;
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

    public static final class Builder {
        private final Rectangle boundary;
        private final Pixel[][] data;

        public Builder(int width, int height) {
            boundary = new Rectangle(width, height);
            data = new Pixel[height][width];
        }

        public void setPixel(Vector point, Pixel value) {
            if (boundary.contains(point)) {
                data[point.getY()][point.getX()] = value;
            }
        }

        public PixelMatrix build() {
            return new ConstantPixelMatrix(data, boundary);
        }
    }
}
