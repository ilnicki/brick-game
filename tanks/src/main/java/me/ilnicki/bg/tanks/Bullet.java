package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.tanks.units.Tank;

public class Bullet extends Entity {
    private final Direction direction;
    private final int speed;
    private final Tank owner;

    public Bullet(Vector point, Direction direction, Tank owner, int speed) {
        super(point);
        this.direction = direction;
        this.speed = speed;
        this.owner = owner;
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
