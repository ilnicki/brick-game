package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public abstract class Entity {
    private Vector pos;

    Entity(Vector pos) {
        this.pos = pos;
    }

    void setPos(Vector pos) {
        this.pos = pos;
    }

    Vector getPos() {
        return pos;
    }

    abstract PixelMatrix getSprite();
}
