package me.ilnicki.bg.core.pixelmatrix;

import java.util.Objects;

public final class Rectangle implements Cloneable {
    private final static Vector DEFAULT_POSITION = new Vector(0, 0);

    private final Vector pos;
    private final int width;
    private final int height;

    public Rectangle(Vector pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int width, int height) {
        this(DEFAULT_POSITION, width, height);
    }

    public Vector getPos() {
        return pos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(Vector point) {
        return point.getX() >= pos.getX()
                && point.getX() < width
                && point.getY() >= pos.getY()
                && point.getY() < height;
    }

    @Override
    protected Object clone() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Rectangle) {
            Rectangle otherRect = (Rectangle) other;
            return this == otherRect
                    || (width == otherRect.width
                    && height == otherRect.height
                    && pos.equals(otherRect.pos)
            );
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, pos);
    }
}
