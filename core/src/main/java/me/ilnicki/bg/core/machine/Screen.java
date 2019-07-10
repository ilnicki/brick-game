package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.Positionable;

public final class Screen implements PixelMatrix, Positionable {

    private final int width;
    private final int height;
    private Field field;
    private Vector position;

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
        position = new Vector(0, 0);
    }

    @Override
    public Pixel getPixel(Vector point) {
        if (point.getX() >= getWidth() || point.getX() < 0
                || point.getY() >= getHeight() || point.getY() < 0) {
            return Pixel.WHITE;
        } else {
            return Pixel.merge(field.getPixel(point.add(position)), Pixel.WHITE);
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
    public void setPos(Vector point) {
        position = point;
    }

    @Override
    public Vector getPos() {
        return position;
    }
}
