package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public abstract class Entity {
    private final Point position;

    public Entity(Point position) {
        this.position = position;
    }

    public Entity(int posX, int posY) {
        this.position = new Point(posX, posY);
    }

    public int getPosX() {
        return this.position.getX();
    }

    public void setPosX(int posX) {
        this.position.setX(posX);
    }

    public int getPosY() {
        return this.position.getY();
    }

    public void setPosY(int posY) {
        this.position.setY(posY);
    }

    public void setPos(int posX, int posY) {
        this.position.set(posX, posY);
    }

    public abstract PixelMatrix getSprite();
}
