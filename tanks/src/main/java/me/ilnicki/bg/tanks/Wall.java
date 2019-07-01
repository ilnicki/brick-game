package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.pixelmatrix.Point;

class Wall extends Entity {
    public Wall(int posX, int posY) {
        super(posX, posY);
    }

    public Wall(Point pos) {
        super(pos);
    }
}
