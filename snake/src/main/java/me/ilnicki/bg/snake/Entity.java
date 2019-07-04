package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public abstract class Entity {
    private Point pos;

    Entity(Point pos) {
        this.pos = pos;
    }

    void setPos(Point pos) {
        this.pos = pos;
    }

    Point getPos() {
        return pos;
    }

    abstract PixelMatrix getSprite();
}
