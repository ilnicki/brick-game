package me.ilnicki.bg.engine.pixelmatrix;

public interface Positionable {
    int getX();

    void setX(int positionX);

    int getY();

    void setY(int positionY);

    void setPosition(int positionX, int positionY);

    void setPosition(Point point);

    Point getPosition();
}
