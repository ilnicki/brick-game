package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.Vector;

public enum Direction {
    UP(new Vector(0, 1)),
    RIGHT(new Vector(1, 0)),
    DOWN(new Vector(0, -1)),
    LEFT(new Vector(-1, 0));

    private final Vector vector;

    Direction(Vector vector) {
        this.vector = vector;
    }

    public Vector getVector() {
        return vector;
    }
}
