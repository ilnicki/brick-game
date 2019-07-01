package me.ilnicki.bg.tanks.units;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.tanks.Bullet;
import me.ilnicki.bg.tanks.Direction;
import me.ilnicki.bg.tanks.Entity;

import java.util.HashMap;

public class Tank extends Entity {
    private Direction direction;
    HashMap<Direction, PixelMatrix> sprites;
    HashMap<Direction, Point> shotPoints;

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
    public boolean isCollide(int x, int y) {
        try {
            return this.getSprite().getPixel(x - this.getPosX(), y - this.getPosY()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PixelMatrix getSprite() {
        return sprites.get(this.direction);
    }

    public Bullet shoot() {
        Point shootPoint = new Point(shotPoints.get(this.getDirection()));
        shootPoint.add(this.getPos());

        return new Bullet(shootPoint, this.getDirection(), this);
    }
}
