package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public class SnakeHead extends SnakePart {
    private static final PixelMatrix spriteBlink = Matrices.fromString(" ");

    private static final int blinkOn = 7;
    private static final int blinkTime = 3;
    private Direction direction;
    private int callCounter = 0;

    public SnakeHead(Vector pos, Direction direction) {
        super(pos);
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
        DIR_UP(new Vector(0, 1)),
        DIR_RIGHT(new Vector(-1, 0)),
        DIR_DOWN(new Vector(0, 1)),
        DIR_LEFT(new Vector(1, 0));

        private final Vector vector;

        Direction(Vector vector) {
            this.vector = vector;
        }

        public Vector getVector() {
            return vector;
        }
    }
}
