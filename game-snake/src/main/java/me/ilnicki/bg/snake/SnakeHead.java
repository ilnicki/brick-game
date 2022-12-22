package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.container.Inject;

public class SnakeHead extends SnakePart {
  @Inject({"assets.sprites.snake.head"})
  private Animation sprite;
  private Direction direction;

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
    return sprite.next();
  }

  public enum Direction {
    DIR_UP(new Vector(0, 1)),
    DIR_RIGHT(new Vector(1, 0)),
    DIR_DOWN(new Vector(0, -1)),
    DIR_LEFT(new Vector(-1, 0));

    private final Vector vector;

    Direction(Vector vector) {
      this.vector = vector;
    }

    public Vector getVector() {
      return vector;
    }
  }
}
