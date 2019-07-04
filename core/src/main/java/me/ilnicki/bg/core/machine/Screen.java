package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.core.pixelmatrix.Positionable;

public final class Screen implements PixelMatrix, Positionable {

    private final int width;
    private final int height;
    private Field field;
    private Point position;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Screen(Field field) {
        this(field.getWidth(), field.getHeight());
        setField(field);
    }

    public Screen(int width, int height, Field field) {
        this(width, height);
        setField(field);
    }

    void setField(Field field) {
        this.field = field;
        position = new Point(0, 0);
    }

    @Override
    public void setPixel(Point point, Pixel value) {
    }

    @Override
    public Pixel getPixel(Point point) {
        if (point.getX() >= getWidth() || point.getX() < 0
                || point.getY() >= getHeight() || point.getY() < 0) {
            return Pixel.WHITE;
        } else {
            return Pixel.merge(field.getPixel(point.add(position)), Pixel.WHITE);
        }
    }

    @Override
    public Pixel getPixel(int x, int y) {
        return getPixel(new Point(x, y));
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
    public void setPos(Point point) {
        position = point;
    }

    @Override
    public Point getPos() {
        return position;
    }
}
