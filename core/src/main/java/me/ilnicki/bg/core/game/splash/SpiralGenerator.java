package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.pixelmatrix.Vector;

public class SpiralGenerator {
    private int width;
    private int height;

    private int posX = 0;
    private int posY = 0;

    private int offsetX = 0;
    private int offsetY = 0;

    private int dirX = 0;
    private int dirY = +1;

    public SpiralGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector next() {
        if(width == 0 || height == 0) {
            return null;
        }

        Vector vector = new Vector(posX + offsetX, posY + offsetY);

        if(posX + dirX >= width || posX + dirX < 0 || posY + dirY >= height || posY + dirY < 0) {
            turnRight();
        }

        if(posY == 0 && posX == 1) {
            width -= 2;
            height -= 2;
            offsetX++;
            offsetY++;
            posX = 0;
            posY = 0;
        } else {
            posX += dirX;
            posY += dirY;
        }

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
