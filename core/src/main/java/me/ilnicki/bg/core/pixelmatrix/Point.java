package me.ilnicki.bg.core.pixelmatrix;

public class Point implements Cloneable, Vector2D {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point add(Point point) {
        return new Point(x + point.x, y + point.y);
    }

    public Point sub(Point point) {
        return new Point(x - point.x, y - point.y);
    }

    public Point withX(int x) {
        return new Point(x, y);
    }

    public Point withY(int y) {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }

    @Override
    protected Object clone() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Point) {
            Point otherPoint = (Point) other;
            return x == otherPoint.x && y == otherPoint.y;
        }

        return false;
    }
}
