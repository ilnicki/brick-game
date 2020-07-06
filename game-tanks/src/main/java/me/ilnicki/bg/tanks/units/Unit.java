package me.ilnicki.bg.tanks.units;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public abstract class Unit {
  private Vector pos;
  private final PixelMatrix sprite = Matrices.fromString("#");

  Unit(Vector pos) {
    this.pos = pos;
  }

  public void setPos(Vector pos) {
    this.pos = pos;
  }

  public Vector getPos() {
    return pos;
  }

  public PixelMatrix getSprite() {
    return sprite;
  }

  public boolean isCollide(Vector point) {
    return pos.equals(point);
  }
}
