package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public class Entity {
    private final Point pos;
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

    public void setPosY(int posY) {
        pos.setY(posY);
    }

    public void setPosX(int posX) {
        pos.setX(posX);
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        pos.setY(pos.getY());
        pos.setX(pos.getX());
    }

    public void setPos(int posY, int posX) {
        this.setPosY(posY);
        this.setPosX(posX);
    }

    public PixelMatrix getSprite() {
        return sprite;
    }

    public boolean isCollide(Point point) {
        return pos.equals(point);
    }
}
