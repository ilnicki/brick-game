package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public class Entity {
    private Point pos;
    private final PixelMatrix sprite = MatrixUtils.fromString("#");

    public Entity(Point pos) {
        this.pos = pos;
    }

    public int getPosY() {
        return pos.getY();
    }

    public int getPosX() {
        return pos.getX();
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    public PixelMatrix getSprite() {
        return sprite;
    }

    public boolean isCollide(Point point) {
        return pos.equals(point);
    }
}
