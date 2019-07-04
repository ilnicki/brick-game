package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.core.pixelmatrix.Vector2D;

public enum Direction {
    UP(new Point(0, 1)),
    RIGHT(new Point(1, 0)),
    DOWN(new Point(0, -1)),
    LEFT(new Point(-1, 0));

    private final Vector2D vector;

    Direction(Vector2D vector) {
        this.vector = vector;
    }

    public Vector2D getVector() {
        return vector;
    }
}
