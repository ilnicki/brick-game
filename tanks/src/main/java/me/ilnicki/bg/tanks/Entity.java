package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public class Entity {
    private Vector pos;
    private final PixelMatrix sprite = MatrixUtils.fromString("#");

    public Entity(Vector pos) {
        this.pos = pos;
    }

    public int getPosY() {
        return pos.getY();
    }

    public int getPosX() {
        return pos.getX();
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public Vector getPos() {
        return pos;
    }

    public PixelMatrix getSprite() {
        return sprite;
    }

    public boolean isCollide(Vector point) {
        return pos.equals(point);
    }
}
