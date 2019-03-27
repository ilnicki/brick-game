package me.ilnicki.bg.engine.pixelmatrix;

public class Point implements Cloneable
{
    private int x;
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(Point point)
    {
        this(point.getX(), point.getY());
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void set(int x, int y)
    {
        setX(x);
        setY(y);
    }

    public void add(Point point)
    {
        setX(getX() + point.getX());
        setY(getY() + point.getY());
    }

    public static Point add(Point point1, Point point2)
    {
        Point result = (Point) point1.clone();
        result.add(point2);

        return result;
    }

    @Override
    public String toString()
    {
        return String.format("[%d, %d]", x, y);
    }

    @Override
    protected Object clone()
    {
        return new Point(x, y);
    }
}
