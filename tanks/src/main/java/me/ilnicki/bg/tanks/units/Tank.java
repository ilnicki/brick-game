package me.ilnicki.bg.tanks.units;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.tanks.Bullet;
import me.ilnicki.bg.tanks.Direction;
import me.ilnicki.bg.tanks.Entity;

import java.util.HashMap;

public class Tank extends Entity {
    HashMap<Direction, PixelMatrix> sprites;
    HashMap<Direction, Point> shotPoints;

    private Direction direction;

    Tank(Point point, Direction direction) {
        super(point);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean isCollide(Point point) {
        try {
            return getSprite().getPixel(point.sub(getPos())) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PixelMatrix getSprite() {
        return sprites.get(this.direction);
    }

    public Bullet shoot() {
        Point shootPoint = shotPoints.get(this.getDirection()).add(this.getPos());
        return new Bullet(shootPoint, this.getDirection(), this, 2);
    }
}
