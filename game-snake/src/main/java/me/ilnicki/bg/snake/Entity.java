package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

abstract class Entity {
  private Vector pos;

  Entity(Vector pos) {
    this.pos = pos;
  }

  void setPos(Vector pos) {
    this.pos = pos;
  }

  Vector getPos() {
    return pos;
  }

  abstract PixelMatrix getSprite();
}
