package me.ilnicki.bg.engine.machine;

import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Point;
import me.ilnicki.bg.engine.pixelmatrix.Positionable;

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
    public void setPixel(int y, int x, Pixel value) {
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
            return Pixel.merge(field.getPixel(Point.add(point, position)), Pixel.WHITE);
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
    public int getX() {
        return position.getX();
    }

    @Override
    public void setX(int positionX) {
        if (positionX <= -getWidth() || positionX >= field.getWidth()) {
            throw new IllegalArgumentException(
                    String.format("Wrong X position: %d. Position must be in range from -%d to %d.",
                            positionX, getWidth(), field.getWidth())
            );
        } else {
            position.setX(positionX);
        }
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public void setY(int positionY) {
        if (positionY <= -getHeight() || positionY >= field.getHeight()) {
            throw new IllegalArgumentException(String.format(
                    "Wrong Y position: %d. Position must be in range from -%d to %d.",
                    positionY, getHeight(), field.getHeight())
            );
        } else {
            position.setY(positionY);
        }
    }

    @Override
    public void setPosition(int positionY, int positionX) {
        setY(positionY);
        setX(positionX);
    }

    @Override
    public void setPosition(Point point) {
        position = point;
    }

    @Override
    public Point getPosition() {
        return position;
    }
}
