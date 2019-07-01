package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.tanks.units.Tank;

public class Bullet extends Entity {
    private final Direction direction;
    private final int speed;
    private Tank owner;

    public Bullet(int posY, int posX, Direction direction, int speed, Tank owner) {
        super(posX, posY);
        this.direction = direction;
        this.speed = speed;
        this.owner = owner;
    }

    public Bullet(Point point, Direction direction, int speed, Tank owner) {
        super(point);
        this.direction = direction;
        this.speed = speed;
        this.owner = owner;
    }

    public Bullet(int posY, int posX, Direction direction, Tank owner) {
        this(posY, posX, direction, 2, owner);
    }

    public Bullet(Point point, Direction direction, Tank owner) {
        this(point, direction, 2, owner);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public Tank getOwner() {
        return owner;
    }
}
