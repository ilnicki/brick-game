package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.pixelmatrix.Vector;

public class SpiralGenerator {
    private int width;
    private int height;

    private int posX = 0;
    private int posY = 0;

    private int dirX = 0;
    private int dirY = +1;

    public SpiralGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector next() {
        if(width == 0 && height == 0) {
            return null;
        }

        Vector vector = new Vector(posX, posY);

        if(posX + dirX == width || posX + dirX < 0 || posY + dirY == height || posY + dirY < 0) {
            turnRight();
        }

        if(posY == 0 && posX == 1) {
            width--;
            height--;
        }

        posX += dirX;
        posY += dirY;

        return vector;
    }

    private void turnRight() {
        int nextDirX = 0;
        int nextDirY = 0;

        if(dirX != 0) {
            nextDirY = -dirX;
        }

        if(dirY != 0) {
            nextDirX = dirY;
        }

        dirX = nextDirX;
        dirY = nextDirY;
    }
}
