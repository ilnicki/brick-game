package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class SnakeHead extends SnakePart {
    private static final PixelMatrix spriteBlink = MatrixUtils.fromString(" ");

    private static final int blinkOn = 7;
    private static final int blinkTime = 3;
    private Direction direction;
    private int callCounter = 0;

    public SnakeHead(int posX, int posY, Direction direction) {
        super(posX, posY);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public PixelMatrix getSprite() {
        if (++this.callCounter % blinkOn < blinkTime) {
            return spriteBlink;
        } else {
            return super.getSprite();
        }
    }

    public enum Direction {
        DIR_UP, DIR_RIGHT, DIR_DOWN, DIR_LEFT
    }
}
