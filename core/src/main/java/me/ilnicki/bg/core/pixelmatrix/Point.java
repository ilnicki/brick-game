package me.ilnicki.bg.core.pixelmatrix;

public class Point implements Cloneable {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this(point.getX(), point.getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public Point add(Point point) {
        return new Point(getX() + point.getX(), getY() + point.getY());
    }

    public Point sub(Point point) {
        return new Point(getX() - point.getX(), getY() - point.getY());
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
            return getX() == otherPoint.getX() && getY() == otherPoint.getY();
        }

        return false;
    }
}
