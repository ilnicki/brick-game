package me.ilnicki.bg.core.math;

import java.util.Objects;

public final class Vector implements Cloneable {
  private final int x;
  private final int y;

  public Vector(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Vector add(Vector point) {
    return new Vector(x + point.x, y + point.y);
  }

  public Vector sub(Vector point) {
    return new Vector(x - point.x, y - point.y);
  }

  public Vector withX(int x) {
    return new Vector(x, y);
  }

  public Vector withY(int y) {
    return new Vector(x, y);
  }

  @Override
  public String toString() {
    return String.format("[%d, %d]", x, y);
  }

  @Override
  protected Object clone() {
    return this;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Vector) {
      Vector otherVector = (Vector) other;
      return x == otherVector.x && y == otherVector.y;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
