package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.core.pixelmatrix.Vector2D;

public class SnakeHead extends SnakePart {
    private static final PixelMatrix spriteBlink = MatrixUtils.fromString(" ");

    private static final int blinkOn = 7;
    private static final int blinkTime = 3;
    private Direction direction;
    private int callCounter = 0;

    public SnakeHead(Point pos, Direction direction) {
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
        DIR_UP(new Point(0, 1)),
        DIR_RIGHT(new Point(-1, 0)),
        DIR_DOWN(new Point(0, 1)),
        DIR_LEFT(new Point(1, 0));

        private final Vector2D vector;

        Direction(Vector2D vector) {
            this.vector = vector;
        }

        public Vector2D getVector() {
            return vector;
        }
    }
}
