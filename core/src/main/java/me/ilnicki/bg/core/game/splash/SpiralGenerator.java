package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.math.Rectangle;
import me.ilnicki.bg.core.math.Vector;

public class SpiralGenerator {
  private static final Vector ZERO = new Vector(0, 0);
  private static final Vector ONE = new Vector(1, 1);
  private static final Vector UP = new Vector(0, 1);
  private static final Vector RIGHT = new Vector(1, 0);

  private Rectangle boundaries;
  private Vector pos = ZERO;
  private Vector offset = ZERO;
  private Vector dir = UP;

  public SpiralGenerator(int width, int height) {
    boundaries = new Rectangle(width, height);
  }

  public boolean hasNext() {
    return boundaries.getArea() > 0;
  }

  public Vector next() {
    if (!hasNext()) {
      return null;
    }

    final Vector vector = pos.add(offset);
    Vector nextPos = pos.add(dir);

    if (!boundaries.contains(nextPos)) {
      turnRight();
      nextPos = pos.add(dir);
    }

    if (pos.equals(RIGHT)) {
      boundaries = new Rectangle(boundaries.getWidth() - 2, boundaries.getHeight() - 2);
      offset = offset.add(ONE);
      pos = ZERO;
    } else {
      pos = nextPos;
    }

    return vector;
  }

  private void turnRight() {
    dir = new Vector(dir.getY(), -dir.getX());
  }
}
